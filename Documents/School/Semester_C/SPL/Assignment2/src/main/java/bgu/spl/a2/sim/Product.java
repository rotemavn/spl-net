package bgu.spl.a2.sim;

import bgu.spl.a2.sim.tools.Tool;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A class that represents a product produced during the simulation.
 */
public class Product implements Serializable {

    private final long _startId;
    private AtomicLong _finalId;
    private String _name;
    private List<Product> productsNeeded;
    private boolean end=false;
    private long add=0;

    /**
     * Constructor
     * @param startId - Product start id
     * @param name - Product name
     */
    public Product(long startId, String name){
        _startId = startId;
        _finalId = new AtomicLong(startId);
        _name = name;
        productsNeeded = new Vector<>();
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
    public synchronized long getFinalId(){
        return _finalId.get();
    }

    /**
     * @return Returns all parts of this product as a List of Products
     */
    public synchronized List<Product> getParts(){return productsNeeded;}

    /**
     * Add a new part to the product
     * @param p - part to be added as a Product object
     */
    public synchronized void addPart(Product p){
        productsNeeded.add(p);
    }

    public void setFinalId(long toAdd){
        if(add==0)
            add=toAdd;
        if(!end) {
            _finalId.getAndAdd(add);
            end=true;
        }
    }

    @Override
    public String toString(){
        String res="ProductName: "+ getName()+"  Product Id = "+_finalId+"\n";
        res+="PartsList {\n";
        for(Product p:getParts()){
            res+=p.toString();
        }
        res+="}\n";
        return res;
    }



}
