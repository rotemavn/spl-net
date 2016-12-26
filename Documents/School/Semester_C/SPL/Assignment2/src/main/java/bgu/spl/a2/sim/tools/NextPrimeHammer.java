package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by rOTEM on 26-Dec-16.
 */
public class NextPrimeHammer implements Tool{

    private String _name;

    public NextPrimeHammer(){
        _name = "np-hammer";
    }


    @Override
    public String getType() {
        return _name;
    }

    @Override
    public long useOn(Product p) {
        long ans = 0;
        ans = findNextPrime(p.getStartId());
        return ans;
    }

    private long findNextPrime(long num){
        while(true){
            boolean isPrime = true;
            num++;
            long sqr = (long)Math.sqrt(num);
            for(AtomicLong i= new AtomicLong(2); i.get()<=sqr; i.incrementAndGet()){
                if((num%i.get())==0){
                    isPrime = false;
                    break;
                }
            }
            if(isPrime)
                return num;
        }
    }
}
