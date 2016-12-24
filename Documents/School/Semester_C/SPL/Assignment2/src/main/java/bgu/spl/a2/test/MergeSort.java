/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.test;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MergeSort extends Task<int[]> {

    private final int[] array;

    public MergeSort(int[] array) {
        this.array = array;
    }

    @Override
    protected void start() {
        //TODO: replace method body with real implementation
        int x=3+5;
    }

    public static void main(String[] args) throws InterruptedException {
//        WorkStealingThreadPool pool = new WorkStealingThreadPool(4);
//        int n = 1000000; //you may check on different number of elements if you like
//        int[] array = new Random().ints(n).toArray();
//
//        MergeSort task = new MergeSort(array);
//
//        CountDownLatch l = new CountDownLatch(1);
//        pool.start();
//        pool.submit(task);
//        task.getResult().whenResolved(() -> {
//            //warning - a large print!! - you can remove this line if you wish
//            System.out.println(Arrays.toString(task.getResult().get()));
//            l.countDown();
//        });
//
//        l.await();
//        pool.shutdown();

        WorkStealingThreadPool pool = new WorkStealingThreadPool(4);
        int [] a=new int[]{4,5,6};
        MergeSort mergeSort1=new MergeSort(a);
        MergeSort mergeSort2=new MergeSort(a);
        MergeSort mergeSort3=new MergeSort(a);
        MergeSort mergeSort4=new MergeSort(a);
        pool.submit(mergeSort1);
        pool.submit(mergeSort2);
        pool.start();
        pool.submit(mergeSort3);
        pool.submit(mergeSort4);

        pool.shutdown();
    }

}
