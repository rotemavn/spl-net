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
import java.util.concurrent.atomic.AtomicLong;

public class ManufacturingTask extends Task<Product> {

    private final Warehouse warehouse;
    private final ManufactoringPlan plan;
    private final Vector<Task<Product>> tasks;
    private final Product mainProduct;
    private AtomicInteger numOfToolsLeft = new AtomicInteger(0);

    public ManufacturingTask(Product mainProduct, Warehouse warehouse, ManufactoringPlan plan) {
        this.mainProduct = mainProduct;
        this.plan = plan;
        this.warehouse = warehouse;
        tasks = new Vector<>();
    }

    /**
     * The function initiates the manufacturing task process
     */
    @Override
    public void start() {
        final String[] partsNeeded = plan.getParts();

        if (partsNeeded.length == 0) {

            complete(mainProduct);
        } else {
            //defining the sub products, add them to the main product, and creating tasks for them
            for (AtomicInteger i = new AtomicInteger(0); i.get() < partsNeeded.length; i.incrementAndGet()) {
                final long id=mainProduct.getStartId();
                Product subProduct = new Product(id + 1, partsNeeded[i.get()]);
                mainProduct.addPart(subProduct); //add the sub part to tha current main product
                ManufactoringPlan input = warehouse.getPlan(partsNeeded[i.get()]); // the plan of the sub-product
                Task<Product> subProductAssemble = new ManufacturingTask(subProduct, warehouse, input);
                synchronized (tasks) {
                    tasks.add(subProductAssemble); // add the task to the collection so when it resolved the main
                    // product will perform the necessary action
                }
                spawn(subProductAssemble); // spawning the new task to the processor
            }
            //when the sub products are finished to assemble, the current main product is ready to assemble
            whenResolved(tasks, () -> {
                AtomicLong toAdd = new AtomicLong(0);
                // acquiring the tools needed
                String[] toolsNeeded = plan.getTools();
                numOfToolsLeft.set(toolsNeeded.length);
                // acquiring the tools needed according to the product plan
                    for (AtomicInteger j = new AtomicInteger(0); j.get() < toolsNeeded.length; j.incrementAndGet()) {
                        Deferred<Tool> tool = warehouse.acquireTool(toolsNeeded[j.get()]);
                        tool.whenResolved(() -> {
                            toAdd.addAndGet(tool.get().useOn(mainProduct));
                            numOfToolsLeft.decrementAndGet();
                            warehouse.releaseTool(tool.get());
                        });
                    }
                //product is assembled
                while(numOfToolsLeft.get()>0);
                synchronized (mainProduct) {
                    mainProduct.setFinalId(toAdd.get());
                }
                complete(mainProduct);
            });
        }


    }

}
