package bgu.spl.a2.sim;

import bgu.spl.a2.sim.tools.Tool;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class that represents a product produced during the simulation.
 */
public class Product {

    private long _startId;
    private long _finalId;
    private String _name;
    private List<Product> productsNeeded;
    private Vector<Tool> toolsNeeded;

    /**
     * Constructor
     * @param startId - Product start id
     * @param name - Product name
     */
    public Product(long startId, String name){
        _startId = startId;
        _name = name;
        productsNeeded = new Vector<>();
        toolsNeeded = new Vector<>();
    }


    /**
     * @return The product name as a string
     */
    public String getName(){
        return _name;
    }


    /**
     * @return The product start ID as a long. start ID should never be changed.
     */
    public long getStartId(){
        return _startId;
    }
    /**
     * @return The product final ID as a long.
     * final ID is the ID the product received as the sum of all UseOn();
     */
    public long getFinalId(){
        _finalId = _startId;
        //sums all the results from the use of the product on it's tools
        for(AtomicInteger i = new AtomicInteger(0);i.get()<toolsNeeded.size();i.incrementAndGet()){
            _finalId += toolsNeeded.get(i.get()).useOn(this);
        }
        //sums all the final id's of the products needed to assemble the current product
        for(AtomicInteger i = new AtomicInteger(0);i.get()<toolsNeeded.size();i.incrementAndGet()){
            _finalId += productsNeeded.get(i.get()).getFinalId();
        }
        return _finalId;
    }

    /**
     * @return Returns all parts of this product as a List of Products
     */
    public List<Product> getParts(){return productsNeeded;}

    /**
     * Add a new part to the product
     * @param p - part to be added as a Product object
     */
    public void addPart(Product p){
        productsNeeded.add(p);
    }

    /**
     * adds the tools needed to assemble the product
     * @param tools - the tools needed to assemble the object
     */
    public void addTools(Vector<Tool> tools){
        toolsNeeded = tools;
    }




}
