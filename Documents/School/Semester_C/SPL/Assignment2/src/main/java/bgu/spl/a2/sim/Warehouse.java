package bgu.spl.a2.sim;
import bgu.spl.a2.sim.tools.*;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.Deferred;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A class representing the warehouse in your simulation
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 */
public class Warehouse {
    private final Vector<String> indexMapping;
    private final Vector<ConcurrentLinkedQueue<Tool>> toolsLists;
    private final Vector<ConcurrentLinkedQueue<Deferred<Tool>>> deferredLists;
    private final ConcurrentHashMap<String,ManufactoringPlan> plans;
    /**
     * Constructor
     */
    public Warehouse(){
        indexMapping=new Vector<>();
        indexMapping.add(0,"gs-driver");
        indexMapping.add(1,"rs-pliers");
        indexMapping.add(2,"np-hammer");

        toolsLists=new Vector<>();
        toolsLists.add(0,new ConcurrentLinkedQueue<>());
        toolsLists.add(1,new ConcurrentLinkedQueue<>());
        toolsLists.add(2,new ConcurrentLinkedQueue<>());

        deferredLists=new Vector<>();
        deferredLists.add(0,new ConcurrentLinkedQueue<>());
        deferredLists.add(1,new ConcurrentLinkedQueue<>());
        deferredLists.add(2,new ConcurrentLinkedQueue<>());

        plans=new ConcurrentHashMap<>();


    }

    /**
     * Tool acquisition procedure
     * Note that this procedure is non-blocking and should return immediatly
     * @param type - string describing the required tool
     * @return a deferred promise for the  requested tool
     */
    public  Deferred<Tool> acquireTool(String type){
        Deferred<Tool> deferred=new Deferred<>();
        int index=indexMapping.indexOf(type);

        ConcurrentLinkedQueue<Tool> toolList=toolsLists.elementAt(index);

        synchronized (toolList) {
            //if the tool exists in the warehouse then resolve immediately
            if (!toolsLists.elementAt(index).isEmpty()) {
                Tool tool = toolsLists.elementAt(index).poll();
                deferred.resolve(tool);
            }

            //if there aren't any available tools in the warehouse
            else {
                deferredLists.elementAt(index).add(deferred);
            }
        }

        return deferred;
    }

    /**
     * Tool return procedure - releases a tool which becomes available in the warehouse upon completion.
     * @param tool - The tool to be returned
     */
    public  void releaseTool(Tool tool){
        String toolName=tool.getType();
        int index=indexMapping.indexOf(toolName);
        ConcurrentLinkedQueue<Deferred<Tool>> deferList=deferredLists.elementAt(index);
        synchronized (deferList) {
            //if there are deferred waiting for this type of tool poll a deferred and resolve it
            if (!deferredLists.elementAt(index).isEmpty()) {
                Deferred<Tool> deferred = deferredLists.elementAt(index).poll();
                deferred.resolve(tool);
            }
            //if there are no tools required by deferres then just add them to the list
            else {
                toolsLists.elementAt(index).add(tool);
            }
        }
    }


    /**
     * Getter for ManufactoringPlans
     * @param product - a string with the product name for which a ManufactoringPlan is desired
     * @return A ManufactoringPlan for product
     */
    public ManufactoringPlan getPlan(String product){
        return plans.get(product);
    }

    /**
     * Store a ManufactoringPlan in the warehouse for later retrieval
     * @param plan - a ManufactoringPlan to be stored
     */
    public void addPlan(ManufactoringPlan plan){
        String planName=plan.getProductName();
        plans.put(planName,plan);
    }

    /**
     * Store a qty Amount of tools of type tool in the warehouse for later retrieval
     * @param tool - type of tool to be stored
     * @param qty - amount of tools of type tool to be stored
     */
    public void addTool(Tool tool, int qty){
        String type=tool.getType();

        if(type.equals("gs-driver"))
            addGcdScrewDriver(qty);
        else if(type.equals("np-hammer"))
            addNextPrimeHammer(qty);
        else if(type.equals("rs-pliers"))
            addRandomSumPliers(qty);
        else{
            throw new IllegalArgumentException("The given tool is not a legal type of tool "+type);
        }
    }

    /**
     * The function initializes the list of GcdScrewDriver
     * @param qty - number of tools to create
     */
    private void addGcdScrewDriver(int qty){
        for(int i=0; i<qty; i++){
            GcdScrewDriver tool=new GcdScrewDriver();
            toolsLists.elementAt(0).add(tool);
        }
    }

    /**
     * The function initializes the list of NextPrimeHammer
     * @param qty - number of tools to create
     */
    private void addNextPrimeHammer(int qty){
        for(int i=0; i<qty; i++){
            NextPrimeHammer tool=new NextPrimeHammer();
            toolsLists.elementAt(2).add(tool);
        }
    }


    /**
     * The function initializes the list of RandomSumPliers
     * @param qty - number of tools to create
     */
    private void addRandomSumPliers(int qty){
        for(int i=0; i<qty; i++){
            RandomSumPliers tool=new RandomSumPliers();
            toolsLists.elementAt(1).add(tool);
        }
    }

}
