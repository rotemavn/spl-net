/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tasks.manufactoringTask;
import bgu.spl.a2.sim.tools.*;
import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

    private static WorkStealingThreadPool pool;
    private final static Warehouse warehouse=new Warehouse();
    private final static ConcurrentLinkedQueue<Vector<manufactoringTask>> waves=new ConcurrentLinkedQueue();
    private static ConcurrentLinkedQueue<Product> results;

    /**
     * Begin the simulation
     * Should not be called before attachWorkStealingThreadPool()
     */
    public static ConcurrentLinkedQueue<Product> start() {
        while(pool==null); //wait until pool is not null
        results=new ConcurrentLinkedQueue<>();

        while(!waves.isEmpty()){
            try {
                Vector<manufactoringTask> currentWave = waves.peek();
                CountDownLatch latch=new CountDownLatch(currentWave.size());
                for (manufactoringTask task: currentWave) {
                    pool.submit(task);
                    task.getResult().whenResolved(() -> {
                        latch.countDown();
                    });
                }
                latch.await();

                while (latch.getCount()>0);
                waves.poll();

            }
            catch (NullPointerException e){

            } catch (InterruptedException e) {

            }

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
            warehouse.addPlan(new ManufactoringPlan(product,toolsToAdd,partsToAdd));
        }
    }


    private static void initWaves(JsonArray wavesArray){
        for(int i=0; i<wavesArray.size(); i++){
            JsonArray wave=(JsonArray)wavesArray.get(i);
            for(int j=0; j<wave.size(); j++){
                JsonObject obj=(JsonObject)wave.get(j);
                String nameOfProduct=obj.get("product").getAsString();
                ManufactoringPlan plan=warehouse.getPlan(nameOfProduct);
                int qty=obj.get("qty").getAsInt();
                long startID=obj.get("startId").getAsLong();

                Vector<manufactoringTask> waveVector=new Vector<>();
                for(int x=0; x<qty; x++){
                    waveVector.add(new manufactoringTask(startID+x,warehouse,plan));
                }
                waves.add(waveVector);
            }

        }
    }

    //TODO:replace void to int
    public static void main(String [] args){
        try {

            Gson gson = new Gson();

            String fileName="simulation.json";

            JsonParser jsonParser=new JsonParser();
            JsonObject jsonObject=(JsonObject) jsonParser.parse(new FileReader(fileName));
            int numOfThreads=gson.fromJson((JsonPrimitive)jsonObject.get("threads"),int.class);

            JsonArray toolsArray=((JsonArray)jsonObject.get("tools"));
            initTools(toolsArray);

            JsonArray plansArray=((JsonArray)jsonObject.get("plans"));
            initPlans(plansArray);

            JsonArray wavesArray=((JsonArray)jsonObject.get("waves"));
            initWaves(wavesArray);
            System.out.print("");

            Simulator simulator=new Simulator();
            WorkStealingThreadPool pool=new WorkStealingThreadPool(numOfThreads);
            simulator.attachWorkStealingThreadPool(pool);

            simulator.start();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        return 0;
    }




}
