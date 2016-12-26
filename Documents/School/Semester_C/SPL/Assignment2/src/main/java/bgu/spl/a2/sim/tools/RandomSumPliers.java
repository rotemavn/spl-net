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
        AtomicLong ans = new AtomicLong(0);
        Random generator = new Random(p.getStartId());
        int run = (int)generator.nextLong()%1000;
        for(AtomicLong i = new AtomicLong(0); i.get()<run ; i.incrementAndGet()){
            Integer toSum = generator.nextInt();
            ans.addAndGet(toSum);
        }
        return ans.get();
    }
}
