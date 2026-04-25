package org.example.forkjoin;

import java.util.concurrent.RecursiveTask;

public class SumTaskDemo extends RecursiveTask<Long> {

    private int[] arr;
    private int start, end;

    public SumTaskDemo(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {

        // BASE CONDITION:
        // If the task is small enough, do it directly (no more splitting)
        if (end - start <= 100) {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += arr[i];
            }
            return sum;
        }

        // SPLIT TASK:
        int mid = (start + end) / 2;

        SumTaskDemo left = new SumTaskDemo(arr, start, mid);
        SumTaskDemo right = new SumTaskDemo(arr, mid, end);

        // FORK:
        // Send left task to another thread
        left.fork();

        // COMPUTE:
        // Continue working on right task in current thread
        long rightResult = right.compute();

        // JOIN:
        // Wait for left result and combine
        long leftResult = left.join();

        return leftResult + rightResult;
    }
}
