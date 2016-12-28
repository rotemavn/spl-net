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

    /**
     * Constructor
     * @param startId - Product start id
     * @param name - Product name
     */
    public Product(long startId, String name){
        _startId = startId;
        _finalId = startId;
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
    public long getFinalId(){
//        for(AtomicInteger i = new AtomicInteger(0);i.get()<productsNeeded.size();i.incrementAndGet()){
//            _finalId += productsNeeded.get(i.get()).getFinalId();
//        }
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

    public void setFinalId(long toAdd){
        _finalId+= toAdd;
    }

    public String toString(){
        String res="ProductName: "+ _name+" Product Id = "+_finalId+"\n";
        res+="PartsList {\n";
        for(Product p:getParts()){
            res+=p.toString();
        }
        res+="}\n";
        return res;
    }





}
