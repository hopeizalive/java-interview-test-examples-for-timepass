package org.example;

import java.util.ArrayList;
import java.util.List;

class Counter {
    private int c;

    public synchronized void increase() {
        c++;
    }

    public int getC() {
        return c;
    }
}

class ThreadCountExample {
    public static void example(String[] args) {
        Counter c = new Counter();
        Runnable runnable = () -> {
            String name = Thread.currentThread().getName();
            for (int i = 0; i < 1000; i++) {
                c.increase();
//                System.out.println(name + " running "+i);
            }
            System.out.println("value of c in thread " + name + " equal to ==> " + c.getC());
        };
        System.out.println("main start");
        List<Thread> threadList = new ArrayList<>();
        for (int a = 0; a < 10; a++) {
            Thread t1 = new Thread(runnable, "lte-" + a);
            t1.start();
            threadList.add(t1);
        }


        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
            }
        }

        System.out.println("main close with final c value " + c.getC());
    }
}
