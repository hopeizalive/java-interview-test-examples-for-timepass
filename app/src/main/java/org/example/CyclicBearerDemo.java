package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

class CyclicDemoClass implements Runnable {

    private String name;
    private int initTime;
    private CyclicBarrier bearer;

    public CyclicDemoClass(String name, int initTime, CyclicBarrier bearer) {
        this.name = name;
        this.initTime = initTime;
        this.bearer = bearer;
        System.out.println("initTime --> "+initTime + " of the "+name);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(initTime);
            System.out.println("service started "+name);
        } catch (InterruptedException e) {

        }
        try {
            bearer.await();
        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
//            throw new RuntimeException(e);
        }
    }
}

public class CyclicBearerDemo {
    public static void main(String[] args) {
        int noOfServices = 4;
        List<CyclicDemoClass> services = new ArrayList<>();
        CyclicBarrier bearer = new CyclicBarrier(noOfServices,() -> {
            System.out.println("All services process completed");
        });
        for (int i = 1; i <= noOfServices; i++) {
            services.add(new CyclicDemoClass("service-"+i, (int) (Math.random() *  i * 1000),bearer));
        }

        for (CyclicDemoClass service : services) {
            new Thread(service).start();
        }


    }
}
