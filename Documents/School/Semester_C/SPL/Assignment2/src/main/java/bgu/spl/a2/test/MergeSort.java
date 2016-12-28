/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.test;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;

public class MergeSort extends Task<int[]> {

	private final int[] array;
	private Vector<Task<int[]>> mergeSortTasks;

	public MergeSort(int[] array) {
		mergeSortTasks = new Vector<>();
		this.array = array;
	}

	@Override
	protected void start() {
		if (array.length > 1) {
			int mid = array.length / 2;
			int leftSize = mid;
			int rightSize = array.length - mid;
			int[] left = new int[leftSize];
			int[] right = new int[rightSize];
			for (int i = 0; i < mid; i++) {
				left[i] = array[i];
			}
			for (int i = mid; i < array.length; i++) {
				right[i - mid] = array[i];
			}
			Task<int[]> leftSort = new MergeSort(left);
			Task<int[]> rightSort = new MergeSort(right);
			spawn(rightSort);
			spawn(leftSort);
			mergeSortTasks.add(rightSort);
			mergeSortTasks.add(leftSort);

			whenResolved(mergeSortTasks, () -> {
				if (array.length > 1) {
					int[] ans = new int[array.length];
					merge(mergeSortTasks.get(0).getResult().get(), mergeSortTasks.get(1).getResult().get(), ans);
					complete(ans);
				} else {
				}
			});
		} else {
			complete(array);
		}

	}


	public static void merge(int[] left, int[] right, int[] arr) {
		try{
			AtomicInteger leftSize = new AtomicInteger(left.length);
			AtomicInteger rightSize =new AtomicInteger(right.length);
			AtomicInteger i = new AtomicInteger(0);
			AtomicInteger j = new AtomicInteger(0);
			AtomicInteger k = new AtomicInteger(0);
			while (i.get() < leftSize.get() && j.get() < rightSize.get()) {
				if (left[i.get()] <= right[j.get()]) {
					arr[k.get()] = left[i.get()];
					i.incrementAndGet();
					k.incrementAndGet();
				} else {
					arr[k.get()] = right[j.get()];
					k.incrementAndGet();
					j.incrementAndGet();
				}
			}
			while (i.get() < leftSize.get()) {
				arr[k.get()] = left[i.get()];
				k.incrementAndGet();
				i.incrementAndGet();
			}
			while (j.get() < rightSize.get()) {
				arr[k.get()] = right[j.get()];
				k.incrementAndGet();
				j.incrementAndGet();
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			System.out.println(e.getCause().toString());
		}
	}

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i <2000; i++) {
			WorkStealingThreadPool pool = new WorkStealingThreadPool(50);
			int n = 400; //you may check on different number of elements if  you like
			int[] array = new Random().ints(n).toArray();
			MergeSort task = new MergeSort(array);

			CountDownLatch l = new CountDownLatch(1);
			pool.start();
			pool.submit(task);

			task.getResult().whenResolved(() -> {
				//warning - a large print!! - you can remove this line if you wish
				//System.out.println(Arrays.toString(task.getResult().get()));
				l.countDown();
			});

			l.await();
			pool.shutdown();
			System.out.println(i);
		}
	}


}