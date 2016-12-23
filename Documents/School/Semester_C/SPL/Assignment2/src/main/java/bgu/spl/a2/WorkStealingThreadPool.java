package bgu.spl.a2;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.Random;

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
	private ArrayList<LinkedBlockingDeque<Task>> queues;
	private Processor[] processors;
	private int numOfProcessors;

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
    	processors=new Processor[nthreads];
    	queues=new ArrayList<LinkedBlockingDeque<Task>>(nthreads);
    	
    	for(int i=0; i<nthreads; i++){
    		queues.add(new LinkedBlockingDeque<Task>());
    		processors[i]=new Processor(i, this);
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
    private int getRandomIndex(){
    	try{
	    	Random r=new Random();
	    	int index=r.nextInt(numOfProcessors);
	    	return index;
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
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start() {
        for(int i=0; i<processors.length; i++){
        	processors[i].run();
        }
    }
    
    /**
     * The function access the queue of some processor and adds a new task to the head of the queue
     * @param id - the ID of the specific processor
     * @param task - the task to add
     */
    protected void submitToProcessor(int id, Task<?> task){
    	queues.get(id).addFirst(task);
    }
    
    /**
     * The function fetches the task at the head of the Processor's task queue
     * @param id the ID of a specific processor
     * @return the task at the head of the queue of the processor
     */
    protected Task<?> fetchTask(int id){
    	return queues.get(id).pollFirst();
    }
    
    /**
     * The function checks if a processor's queue is empty
     * @param id - of the processor
     * @return true if the queue is empty
     */
    protected boolean isQueueEmpty(int id){
    	return queues.get(id).isEmpty();
    }

}
