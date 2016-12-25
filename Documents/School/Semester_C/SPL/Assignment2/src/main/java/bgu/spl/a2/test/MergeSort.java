/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.test;

import java.util.Vector;
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
//					for(int i=0;i<ans.length;i++){
//						System.out.print(ans[i]+" ");
//					}
					System.out.print("a");
					complete(ans);
				} else {
				}
			});
		} else {
			complete(array);
		}

	}

	// public static void mergeSort(int[] inputArray) {
	// int size = inputArray.length;
	// if (size < 2)
	// return;
	// int mid = size / 2;
	// int leftSize = mid;
	// int rightSize = size - mid;
	// int[] left = new int[leftSize];
	// int[] right = new int[rightSize];
	// for (int i = 0; i < mid; i++) {
	// left[i] = inputArray[i];
	//
	// }
	// for (int i = mid; i < size; i++) {
	// right[i - mid] = inputArray[i];
	// }
	// mergeSort(left);
	// mergeSort(right);
	// merge(left, right, inputArray);
	// }

	public static void merge(int[] left, int[] right, int[] arr) {
		int leftSize = left.length;
		int rightSize = right.length;
		int i = 0, j = 0, k = 0;
		while (i < leftSize && j < rightSize) {
			if (left[i] <= right[j]) {
				arr[k] = left[i];
				i++;
				k++;
			} else {
				arr[k] = right[j];
				k++;
				j++;
			}
		}
		while (i < leftSize) {
			arr[k] = left[i];
			k++;
			i++;
		}
		while (j < leftSize) {
			arr[k] = right[j];
			k++;
			j++;
		}
	}

	public static void main(String[] args) throws InterruptedException {

		WorkStealingThreadPool pool = new WorkStealingThreadPool(2);
		int[] a = { 59, 12,89,4 };
		MergeSort mergeSort1 = new MergeSort(a);
		pool.submit(mergeSort1);
		pool.start();

		pool.shutdown();

		// WorkStealingThreadPool pool = new WorkStealingThreadPool(4);
		// int n = 1000000; //you may check on different number of elements if
		// you like
		// int[] array = new Random().ints(n).toArray();
		//
		// MergeSort task = new MergeSort(array);
		//
		// CountDownLatch l = new CountDownLatch(1);
		// pool.start();
		// pool.submit(task);
		// task.getResult().whenResolved(() -> {
		// //warning - a large print!! - you can remove this line if you wish
		// System.out.println(Arrays.toString(task.getResult().get()));
		// l.countDown();
		// });
		//
		// l.await();
		// pool.shutdown();
	}

}
