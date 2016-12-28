package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.math.BigInteger;

public class GcdScrewDriver implements Tool{

    private String _name;

    public GcdScrewDriver(){
        _name = "gs-driver";
    }


    @Override
    public String getType() {
        return _name;
    }

    @Override
    public long useOn(Product p){
        long value=0;
        for(Product part : p.getParts()){
            value+=Math.abs(func(part.getFinalId()));

        }
        return value;
    }

    /**
     *
     * @param id current ID of the product
     * @return the calculation of GcdScrewDriver
     */
    public long func(long id){
        BigInteger b1 = BigInteger.valueOf(id);
        BigInteger b2 = BigInteger.valueOf(reverse(id));
        return (b1.gcd(b2)).longValue();

    }

    /**
     *
     * @param n numeric value
     * @return a new numeric value with the reversed digits of n
     */
    private long reverse(long n){
        long reverse=0;
        while( n != 0 ){
            reverse = reverse * 10;
            reverse = reverse + n%10;
            n = n/10;
        }
        return reverse;
    }

}
