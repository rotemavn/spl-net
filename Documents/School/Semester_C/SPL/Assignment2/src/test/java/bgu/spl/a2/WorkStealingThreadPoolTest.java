package bgu.spl.a2;

import bgu.spl.a2.test.MergeSort;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.Assert.*;

public class WorkStealingThreadPoolTest {

    private WorkStealingThreadPool pool;
    private int [] a=new int[]{4,5,6};
    private MergeSort mergeSort=new MergeSort(a);
    @Before
    public void setUp() throws Exception {
        pool=new WorkStealingThreadPool(4);
    }

    @After
    public void tearDown() throws Exception {
//        pool=null;
        assertNull(pool);
    }

    @Test
    public void submit() throws Exception {
//        System.out.println("Submit Test");
//        pool.submit(mergeSort);
//
//        for (LinkedBlockingDeque<Task> q:pool.getQueues()
//             ) {
//            System.out.println(q.size());
//        }
//
//        System.out.println("********");
    }



    @Test
    public void shutdown() throws Exception {

    }

    @Test
    public void start() throws Exception {

    }

    @Test
    public void submitToProcessor() throws Exception {
//        System.out.println("submitToProcessor Test");
//        try {
//            pool.submitToProcessor(5, mergeSort);
//        }
//        catch(IndexOutOfBoundsException e){
//            System.out.println("Excetion was catched");
//        }
//
//        for (LinkedBlockingDeque<Task> q:pool.getQueues()
//                ) {
//            System.out.println(q.size());
//        }
//
//        System.out.println("********");
    }

    @Test
    public void fetchTask() throws Exception {
//        System.out.println("fetchTask Test");
//        pool.submitToProcessor(2, mergeSort);
//        System.out.println(pool.fetchTask(3).toString());
//        System.out.println("********");
    }

    @Test
    public void isQueueEmpty() throws Exception {

    }

    @Test
    public void steal() throws Exception {

    }

    @Test
    public void getRandomIndex()throws Exception {
//        System.out.println("Random Index Test");
//        System.out.println(pool.getRandomIndex());
//        System.out.println(pool.getRandomIndex());
//        System.out.println(pool.getRandomIndex());
//        System.out.println(pool.getRandomIndex());
//        System.out.println(pool.getRandomIndex());
//        System.out.println("********");
    }

    @Test
    public void getVictimsQueueID() throws Exception{
        System.out.println("getVictimsQueueID Test");
        pool.submitToProcessor(2, mergeSort);
        assertEquals(pool.getVictimsQueueID(4),2);
        System.out.println("********");
    }




}