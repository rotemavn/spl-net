package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

/**
 * Created by rOTEM on 26-Dec-16.
 */
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
    public long useOn(Product p) {
        long ans = 0;
        ans = gcd(p.getStartId(),reverseNumber(p.getStartId()));
        return ans;
    }

    public long gcd(long a, long b){
        while(a!=0 && b!=0) // until either one of them is 0
            {
                long c = b;
                b = a%b;
                a = c;
            }
        return a+b; // either one is 0, so return the non-zero value
    }

    private long reverseNumber(long n ){
        long reverse = 0;
        while( n != 0 )
        {
            reverse = reverse * 10;
            reverse = reverse + n%10;
            n = n/10;
        }
        return reverse;
    }

}
