/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.WorkStealingThreadPool;
import com.google.gson.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

    private static WorkStealingThreadPool pool;

    /**
     * Begin the simulation
     * Should not be called before attachWorkStealingThreadPool()
     */
    public static ConcurrentLinkedQueue<Product> start(){return null;}


    /**
     * attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
     * @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
     */
    public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool){
        pool=myWorkStealingThreadPool;
    }

    //TODO
    private static void initTools(JsonArray toolsArray){
        for(int i=0; i<toolsArray.size(); i++){

        }
    }

    //TODO
    private static void initPlans(JsonArray plansArray){
        for(int i=0; i<plansArray.size(); i++){

        }
    }

    //TODO
    private static void initWaves(JsonArray wavesArray){
        for(int i=0; i<wavesArray.size(); i++){

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

            //
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        return 0;
    }




}
