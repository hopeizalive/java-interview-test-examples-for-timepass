package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class LatchDemoRunnable implements Callable {
    CountDownLatch downLatch;
    public LatchDemoRunnable(CountDownLatch downLatch, Integer tid){
        System.out.println("LatchDemo constructor -- "+tid);
        this.downLatch = downLatch;
    }

//    @Override
//    public void run() {
//        System.out.println("LatchDemo call ");
//        downLatch.countDown();
//
//    }

    @Override
    public Object call() throws Exception {
        System.out.println("LatchDemo call ");
        downLatch.countDown();

        return null;
    }
}
public class LambdaDemo {
    public static void main(String[] args) throws Exception {
//        example1();
//        example2();
//        scheduleDemo();
        latchDemo();
        System.out.println("main ended");
    }

    private static void latchDemo() throws Exception {
        int nThreads = 14;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        CountDownLatch latch = new CountDownLatch(nThreads);
        for (int i = 1; i <= nThreads; i++) {
            int finalI = i;
            executorService.submit(new LatchDemoRunnable(latch,finalI));
        }


        System.out.println("latch demo methods loaded ");
        latch.await();
        executorService.shutdown();
//        List<Future<LatchDemo>> futures = executorService.invokeAll(callables);
        System.out.println("after invoke all method");
    }

    private static void scheduleDemo() throws InterruptedException {
        System.out.println("scheduleDemo");
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println(LocalDateTime.now());
        },
                5,5,TimeUnit.SECONDS);
        Thread.sleep(10000);
        scheduledExecutorService.shutdown();
    }

    private static void example2() {
        System.out.println("new example");
        ExecutorService executorService = Executors.newFixedThreadPool(3);


        List<Callable<Integer>> callables = new ArrayList<>();
        for (int i = 1; i < 50; i = i + 2) {
            int finalI = i;
            callables.add(() -> {
                System.out.println("value we get is " + finalI);
                if (finalI == 33) {
                    throw new RuntimeException("it is not what i want");
                }
                return finalI;
            });
        }


        try {
            List<Future<Integer>> futures = executorService.invokeAll(callables);
            Integer sum = 0;
            for (Future<Integer> future : futures) {
                Integer value = future.get();
                sum += value;
            }
            System.out.println("all result get ==> " + sum);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        executorService.shutdown();
    }

    private static void example1() throws InterruptedException, ExecutionException {
        long currentTimeMillis = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
//        Executor executorService = Executors.newFixedThreadPool(4);

        for (int i = 2; i < 10; i++) {
            int finalI = i;
            Future<?> submit = executorService.submit(() -> System.out.println(factorial(finalI)));
            submit.get();
//            executorService.execute(() -> System.out.println(factorial(finalI)));
        }
        executorService.shutdown();
        boolean shutdown = executorService.awaitTermination(10, TimeUnit.SECONDS);
        if (shutdown) {

            long timeTaken = System.currentTimeMillis() - currentTimeMillis;
            System.out.println("Time taken --> " + timeTaken);
        }
    }

    public static long factorial(int n) {
        long r = 1;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 1; i < n; i++) {
            r = r * i;
        }
        return r;
    }
}
