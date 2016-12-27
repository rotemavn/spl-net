package bgu.spl.a2;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * this class represents a single work stealing processor, it is
 * {@link Runnable} so it is suitable to be executed by threads.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *
 */
public class Processor implements Runnable {

    private final WorkStealingThreadPool pool;
    private final int id;
    private boolean shouldRun=true;

    /**
     * constructor for this class
     *
     * IMPORTANT:
     * 1) this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     *
     * 2) you may not add other constructors to this class
     * nor you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param id - the processor id (every processor need to have its own unique
     * id inside its thread pool)
     * @param pool - the thread pool which owns this processor
     */
    /*package*/ Processor(int id, WorkStealingThreadPool pool) {
        this.id = id;
        this.pool = pool;

    }

    @Override
    public void run() {
        Task<?> currTask;
        while(!pool.stopRun) {
            currTask = pool.fetchTask(id);
            if(currTask!=null){
                currTask.handle(this);
            }




//            try{
//                while(!pool.isQueueEmpty(id)) {
//                    pool.fetchTask(id).handle(this);
//                }
//                int ver = pool.versionMonitor.getVersion();
//                steal(ver);
//            }
//
//            catch(Exception e){
//                Thread.currentThread().interrupt();
//                shouldRun=false;
//            }
        }
//        System.out.println("Stopped");
    }

    /**
     * The function adds a task to this processor's tasks queue
     * @param task a task to add to the head of the queue
     */
    protected void scheduleTask(Task<?>... task){
        for (Task t:task) {
            pool.submitToProcessor(id, t);
        }

    }

    private void steal(int ver){
        final int numOfProcessors=pool.getNumOfProcessors();
        AtomicBoolean stealSuccedded=new AtomicBoolean(false);
        AtomicInteger stealAttempts=new AtomicInteger(0);
        while (!stealSuccedded.get()&& stealAttempts.get()<numOfProcessors){
            int victimID=((id+stealAttempts.get()+1)%numOfProcessors);
            int numOfTasksStolen=pool.steal(id,victimID);
            if(numOfTasksStolen>0){
                stealSuccedded.set(true);
            }
            else {
               stealAttempts.getAndIncrement();
            }
        }
        if(stealAttempts.get()==numOfProcessors){
            pool.poolWait(ver);
        }

    }

//    protected void suspend(Task<?> task){
//        if(!pool.isQueueEmpty(id) && !Thread.currentThread().isInterrupted()){
//            Task t=pool.fetchTask(id);
//            pool.submitToProcessor(id,task);
//            t.handle(this);
//        }
//        else {
//            pool.submitToProcessor(id,task);
//            steal();
//        }
//    }

}