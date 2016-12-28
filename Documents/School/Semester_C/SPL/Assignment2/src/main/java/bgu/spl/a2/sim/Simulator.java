/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tasks.ManufacturingTask;
import bgu.spl.a2.sim.tools.*;
import com.google.gson.*;


import java.io.*;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

    private static WorkStealingThreadPool pool;
    private final static Warehouse warehouse=new Warehouse();
    private final static ConcurrentLinkedQueue<Vector<ManufacturingTask>> waves=new ConcurrentLinkedQueue();
    private static ConcurrentLinkedQueue<Product> results;

    /**
     * Begin the simulation
     * Should not be called before attachWorkStealingThreadPool()
     */
    public static ConcurrentLinkedQueue<Product> start() {
        while(pool==null); //wait until pool is not null
        pool.start();
        results=new ConcurrentLinkedQueue<>();

        while(!waves.isEmpty()){
            try {
                Vector<ManufacturingTask> currentWave = waves.peek();
                CountDownLatch latch=new CountDownLatch(currentWave.size());
                Product[] manufactured=new Product[currentWave.size()];
                for (ManufacturingTask task: currentWave) {
                    pool.submit(task);
                    task.getResult().whenResolved(() -> {
                        manufactured[currentWave.indexOf(task)]=task.getResult().get();
                        latch.countDown();
                    });
                }
                latch.await();

                for(int i=0; i<manufactured.length ;i++){
                    results.add(manufactured[i]);
                }
                waves.poll();

            }

            catch (InterruptedException e) {}

        }

        try {
            pool.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }


    /**
     * attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
     * @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
     */
    public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool){
        pool=myWorkStealingThreadPool;
    }

    /**
     * The function finds the requirements for the tools in the current run according to the JSON file
     * @param toolsArray Json array given as input for tools
     */
    private static void initTools(JsonArray toolsArray){
        for(int i=0; i<toolsArray.size(); i++){
           JsonObject obj=(JsonObject)toolsArray.get(i);
           String name=obj.get("tool").getAsString();
           int qty=obj.get("qty").getAsInt();

           Tool t;
           if(name.equals("gs-driver")){
               t=new GcdScrewDriver();
           }
           else if(name.equals("rs-pliers")){
               t=new RandomSumPliers();
           }

           else{
               t=new NextPrimeHammer();
           }

            warehouse.addTool(t,qty);

        }
    }

    /**
     * The function finds the requirements for the tools in the current run according to the JSON file
     * @param plansArray Json array given as input for tools
     */
    private static void initPlans(JsonArray plansArray){
        for(int i=0; i<plansArray.size(); i++){
            JsonObject obj=(JsonObject)plansArray.get(i);
            String product=obj.get("product").getAsString();

            JsonArray tools=obj.get("tools").getAsJsonArray();
            String[] toolsToAdd=new String[tools.size()];
            for(int j=0; j<tools.size(); j++){
                JsonPrimitive toolObj=(JsonPrimitive)tools.get(j);
                String toolName=toolObj.getAsString();
                toolsToAdd[j]=toolName;
            }


            JsonArray parts=obj.get("parts").getAsJsonArray();
            String[] partsToAdd=new String[parts.size()];
            for(int j=0; j<parts.size(); j++){
                JsonPrimitive partsObj=(JsonPrimitive)parts.get(j);
                String partName=partsObj.getAsString();
                partsToAdd[j]=partName;
            }
            warehouse.addPlan(new ManufactoringPlan(product,partsToAdd,toolsToAdd));
        }
    }

    /**
     * The function finds the requirements for the waves in the current run according to the JSON file
     * @param wavesArray Json array given as input for tools
     */
    private static void initWaves(JsonArray wavesArray){
        for(int i=0; i<wavesArray.size(); i++){
            JsonArray wave=(JsonArray)wavesArray.get(i);
            Vector<ManufacturingTask> waveVector=new Vector<>();
            for(int j=0; j<wave.size(); j++){
                JsonObject obj=(JsonObject)wave.get(j);
                String nameOfProduct=obj.get("product").getAsString();
                ManufactoringPlan plan=warehouse.getPlan(nameOfProduct);
                int qty=obj.get("qty").getAsInt();
                long startID=obj.get("startId").getAsLong();


                for(int x=0; x<qty; x++){
                    Product product=new Product(startID+x,nameOfProduct);
                    waveVector.add(new ManufacturingTask(product,warehouse,plan));
                }

            }
            waves.add(waveVector);

        }
    }

    protected static void writeToFile(ConcurrentLinkedQueue<Product> result){
        try {
            FileOutputStream fout = new FileOutputStream("result.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            for (Product product:result) {
                String ans=product.toString();
                oos.writeObject(ans+"\n");
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO:replace void to int
    public static void main(String [] args) {
        try {
            for(int i=0; i<50; i++) {
                Gson gson = new Gson();

                String fileName = "simulation.json";

                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParser.parse(new FileReader(fileName));
                int numOfThreads = gson.fromJson((JsonPrimitive) jsonObject.get("threads"), int.class);

                JsonArray toolsArray = ((JsonArray) jsonObject.get("tools"));
                initTools(toolsArray);

                JsonArray plansArray = ((JsonArray) jsonObject.get("plans"));
                initPlans(plansArray);

                JsonArray wavesArray = ((JsonArray) jsonObject.get("waves"));
                initWaves(wavesArray);
                System.out.print("");

                Simulator simulator = new Simulator();
                WorkStealingThreadPool pool = new WorkStealingThreadPool(numOfThreads);
                simulator.attachWorkStealingThreadPool(pool);

//                writeToFile(simulator.start());
                ConcurrentLinkedQueue<Product> result=simulator.start();
                for (Product product:result) {
                    String ans=product.toString();
                    System.out.println(ans);
                }
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


//        return 0;
        }

    }





