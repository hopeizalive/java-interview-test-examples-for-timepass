package org.example;

class SharedData {
    private int data;
    private boolean hasData;

    public synchronized void produce(int data) {
        while (hasData) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        this.data = data;
        hasData = true;
        System.out.println("produce " + data);
        notify();
    }

    public synchronized int consume() {
        while (!hasData) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        hasData = false;
        System.out.println("Consume " + data);
        notify();
        return data;
    }
}

public class ThreadCommunicationExample {
    public static void main(String[] args) {
        SharedData sharedData = new SharedData();
        Thread producerThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                sharedData.produce(i);
                sleep();
            }
        });

        Thread consumerThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                sharedData.consume();
                sleep();
            }

        });
        producerThread.start();
        consumerThread.start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
