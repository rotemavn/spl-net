package bgu.spl.a2.sim.Tasks;

import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.ManufactoringPlan;

/**
 * Created by itama_000 on 12/26/2016.
 */
public class manufactoringTask extends Task<Product>{

    private Warehouse warehouse;
    private ManufactoringPlan plan;
    private long startId;


    public manufactoringTask(long startId, Warehouse warehouse, ManufactoringPlan plan){
        this.startId = startId;
        this.plan = plan;
        this.warehouse = warehouse;
    }

    @Override
    protected void start() {

    }
}
