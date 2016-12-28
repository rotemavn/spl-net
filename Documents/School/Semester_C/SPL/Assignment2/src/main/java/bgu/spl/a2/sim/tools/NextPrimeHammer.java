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
        long value=0;
        for(Product part : p.getParts()){
            value+=Math.abs(func(part.getFinalId()));

        }
        return value;
    }

    /**
     *
     * @param id current ID of the product
     * @return the calculation of NextPrimeHammer
     */
    public long func(long id) {

        long v =id + 1;
        while (!isPrime(v)) {
            v++;
        }

        return v;
    }

    /**
     *
     * @param value numeric value
     * @return true if value is a prime number
     */
    private boolean isPrime(long value) {
        if(value < 2) return false;
        if(value == 2) return true;
        long sq = (long) Math.sqrt(value);
        for (long i = 2; i <= sq; i++) {
            if (value % i == 0) {
                return false;
            }
        }

        return true;
    }
}
