package bgu.spl.a2;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * represents a work stealing thread pool - to understand what this class does
 * please refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class WorkStealingThreadPool {
	private final ArrayList<LinkedBlockingDeque<Task>> queues;
	private final Thread[] processors;
	private final int numOfProcessors;
	private final VersionMonitor versionMonitor;
	private boolean hasTasks=false;

    /**
     * creates a {@link WorkStealingThreadPool} which has nthreads
     * {@link Processor}s. Note, threads should not get started until calling to
     * the {@link #start()} method.
     *
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param nthreads the number of threads that should be started by this
     * thread pool
     */
    public WorkStealingThreadPool(int nthreads) {
    	numOfProcessors=nthreads;
    	processors=new Thread[nthreads];
    	queues=new ArrayList<LinkedBlockingDeque<Task>>(nthreads);
        versionMonitor=new VersionMonitor();
    	
    	for(int i=0; i<nthreads; i++){
    		queues.add(new LinkedBlockingDeque<Task>());
    		processors[i]=new Thread(new Processor(i, this));
    	}
    }

    /**
     * submits a task to be executed by a processor belongs to this thread pool
     *
     * @param task the task to execute
     */
    public void submit(Task<?> task) {
    
       int processorIndex=getRandomIndex();
       if(processorIndex>-1)
       {
    	   queues.get(processorIndex).addFirst(task);
           versionMonitor.inc();
           if(!hasTasks){
        	   hasTasks=true;
        	   start();
           }
       }
       else
       {
    	   throw new IllegalStateException("The number of processors is not valid");
       }
    }
    
    /**
     * The function generates a random integer between 0 and the number of processors
     * @return an integer value
     */
    protected int getRandomIndex(){
    	try{
            return ThreadLocalRandom.current().nextInt(numOfProcessors);

    	}
    	catch(Exception e){
    		return -1;
    	}
    }

    /**
     * closes the thread pool - this method interrupts all the threads and wait
     * for them to stop - it is returns *only* when there are no live threads in
     * the queue.
     *
     * after calling this method - one should not use the queue anymore.
     *
     * @throws InterruptedException if the thread that shut down the threads is
     * interrupted
     * @throws UnsupportedOperationException if the thread that attempts to
     * shutdown the queue is itself a processor of this queue
     */
    public void shutdown() throws InterruptedException {
        for (Thread t:processors) {
            t.interrupt();
        }

    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start() {
    	if(hasTasks){
	        for (Thread p:processors) {
	            p.start();
	        }
    	}
    }
    
    /**
     * The function access the queue of some processor and adds a new task to the head of the queue
     * @param id - the ID of the specific processor
     * @param task - the task to add
     */
     void submitToProcessor(int id, Task<?> task){
        if(id>=numOfProcessors)
            throw new IndexOutOfBoundsException("The ID provided exeeded the number of availible processord");
        else


        try{
            queues.get(id).addFirst(task);
            versionMonitor.inc();
        }
        catch (IndexOutOfBoundsException e){}
    }
    
    /**
     * The function fetches the task at the head of the Processor's task queue
     * @param id the ID of a specific processor
     * @return the task at the head of the queue of the processor
     */
     Task<?> fetchTask(int id){
    	try {
    	    return queues.get(id).pollFirst();
        }
        catch (Exception e){
    	    if(queues.get(id).isEmpty())
    	        System.out.println("Processor with ID "+id+" is empty");
    	    else
                System.out.println("Processor with ID "+id+" does not exist in the pool");
        }
        return null;
    }
    
    /**
     * The function checks if a processor's queue is empty
     * @param id - of the processor
     * @return true if the queue is empty
     */
     boolean isQueueEmpty(int id){
    	return queues.get(id).isEmpty();
    }

     void steal(int thiefID){
        AtomicInteger victimID=new AtomicInteger(getVictimsQueueID(thiefID));
        AtomicInteger half=new AtomicInteger(queues.get(victimID.get()).size()/2);
        AtomicInteger tasksStolenCounter=new AtomicInteger(0);

        while (half.get()>0 && queues.get(victimID.get()).size()>0){
        	try{
	            queues.get(thiefID).addFirst(queues.get(victimID.get()).pollLast());
	            tasksStolenCounter.getAndIncrement();
	            half.getAndDecrement();

        	}
        	catch(Exception e)
        	{

        	}
        }
        //if the thief could'nt steal any task
        if(tasksStolenCounter.get()==0){
            try {
                int version=versionMonitor.getVersion();
                versionMonitor.await(version);
            }
            catch (InterruptedException ignore){
//                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * The function searches the next non-empty queue to be the origin to steal from
     * @param thiefID the processor ID who is trying to steal
     * @return the non empty queue we can steal from
     */
     int getVictimsQueueID(int thiefID){
        AtomicInteger victimID=new AtomicInteger((thiefID+1)%numOfProcessors);
            while (queues.get(victimID.get()).isEmpty() || victimID.get() != thiefID) {
                if (victimID.get() == thiefID) {
                	try{
                        int version=versionMonitor.getVersion();
                        versionMonitor.await(version);
                	}
                	catch(InterruptedException ex){

                	}
                }
                victimID.set(victimID.incrementAndGet() % numOfProcessors);
            }
        return victimID.get();

    }


}
