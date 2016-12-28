package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.Tool;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by itama_000 on 12/26/2016.
 */
public class ManufacturingTask extends Task<Product> {

    private Warehouse warehouse;
    private ManufactoringPlan plan;
    private Vector<Task<Product>> tasks;
    private Product mainProduct;
    private AtomicInteger numOfToolsLeft = new AtomicInteger(0);
    public ManufacturingTask(Product mainProduct, Warehouse warehouse, ManufactoringPlan plan) {
        this.mainProduct = mainProduct;
        this.plan = plan;
        this.warehouse = warehouse;
        tasks = new Vector<>();
    }

    @Override
    public void start() {
        System.out.println("manufacturing start");
        String[] partsNeeded = plan.getParts();

        if (partsNeeded.length == 0) {
            complete(mainProduct);
        } else {
            //defining the sub products, add them to the main product, and creating tasks for them
            for (AtomicInteger i = new AtomicInteger(0); i.get() < partsNeeded.length; i.incrementAndGet()) {
                Product subProduct = new Product(mainProduct.getStartId() + 1, partsNeeded[i.get()]);
                mainProduct.addPart(subProduct); //add the sub part to tha current main product
                ManufactoringPlan input = warehouse.getPlan(partsNeeded[i.get()]); // the plan of the sub-product
                Task<Product> subProductAssemble = new ManufacturingTask(subProduct, warehouse, input);
                tasks.add(subProductAssemble); // add the task to the collection so when it resolved the main
                // product will perform the necessary action
                spawn(subProductAssemble); // spawning the new task to the processor
            }
            //when the sub products are finished to assemble, the current main product is ready to assemble
            whenResolved(tasks, () -> {
                // acquiring the tools needed
                String[] toolsNeeded = plan.getTools();
                numOfToolsLeft.set(toolsNeeded.length);
              //  Vector<Deferred<Tool>> tools = new Vector<>();
                // acquiring the tools needed according to the product plan
                for (AtomicInteger i = new AtomicInteger(0); i.get() < toolsNeeded.length; i.incrementAndGet()) {
                    Deferred<Tool> tool = warehouse.acquireTool(toolsNeeded[i.get()]);
                    tool.whenResolved(()->{
                        for(int j =0;j<tasks.size();j++){
                            mainProduct.setFinalId(tool.get().useOn(tasks.get(j).getResult().get()));
                        }
                        numOfToolsLeft.decrementAndGet();
                        warehouse.releaseTool(tool.get());
                    });
                   // tools.add(tool);
                }

                //verifying all the tools acquired are indeed resolved
//                boolean allResolved = true;
//                while (allResolved) {
//                    allResolved = true;
//                    for (AtomicInteger i = new AtomicInteger(0); i.get() < tools.size(); i.incrementAndGet()) {
//                        if (!tools.get(i.get()).isResolved()) {
//                            allResolved = false;
//                            break;
//                        }
//
//                    }
//                }
//                List<Product> finishedSubProducts = mainProduct.getParts();
//                //for each tool , activating the useOn function on all sub products
//                for (AtomicInteger i = new AtomicInteger(0); i.get() < tools.size(); i.incrementAndGet()) {
//                    for (AtomicInteger j = new AtomicInteger(0); j.get() < finishedSubProducts.size(); j.incrementAndGet()) {
//                        mainProduct.setFinalId(tools.get(i.get()).get().useOn(finishedSubProducts.get(j.get())));
//                    }
//                }
//                //after assembling the current main product, releasing the tools acquired
//                for (AtomicInteger i = new AtomicInteger(0); i.get() < tools.size(); i.incrementAndGet()) {
//                    warehouse.releaseTool(tools.get(i.get()).get());
//                }
                //product is assembled
                while(numOfToolsLeft.get()!=0);
                complete(mainProduct);
            });
        }


    }

    public String toString(){
        return "Manufacturing Task";
    }
}
