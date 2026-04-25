package org.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReenterExample {
    Lock d = new ReentrantLock();

    public void o1() {
        d.lock();
        try {
            System.out.println("o1");
            o2();
        } finally {
            System.out.println("o1 unlock");
            d.unlock();
        }
    }

    public void o2() {
        d.lock();
        try {
            System.out.println("o2");
            o3();
        } finally {

            System.out.println("o2 unlock");
            d.unlock();
        }
    }

    public void o3() {
        d.lock();
        try {
            System.out.println("o3");
        } finally {

            System.out.println("o3 unlock");
            d.unlock();
        }
    }

    public static void noidea(String[] args) {
        ReenterExample r = new ReenterExample();
        r.o1();
        System.out.println("no idea");
    }
}
