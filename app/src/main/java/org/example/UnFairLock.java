package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UnFairLock {
    Lock lock = new ReentrantLock(true);
    public void access(){
        lock.lock();
        String threadName = Thread.currentThread().getName();
        try{
            System.out.println("access resource by "+threadName );
        }finally {
            System.out.println("release lock "+threadName);
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        UnFairLock unFairLock =  new UnFairLock();
        Runnable t = () -> {
          unFairLock.access();
        };
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread(t,"dev-"+i);
            list.add(thread);
            thread.start();

        }




    }
}
