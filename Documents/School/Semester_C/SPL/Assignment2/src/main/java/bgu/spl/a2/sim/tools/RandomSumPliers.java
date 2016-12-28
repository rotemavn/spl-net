package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by rOTEM on 26-Dec-16.
 */
public class RandomSumPliers implements Tool {

    private String _name;

    public RandomSumPliers(){
        _name = "rs-pliers";
    }


    @Override
    public String getType() {
        return _name;
    }

    @Override
    public long useOn(Product p) {
        long value=0;
        for(Product part : p.getParts()){
            value+=Math.abs(func(part.getStartId()));

        }
        return value;
    }

    public long func(long id){
        Random r = new Random(id);
        long  sum = 0;
        for (long i = 0; i < id % 10000; i++) {
            sum += r.nextInt();
        }

        return sum;
    }
}
