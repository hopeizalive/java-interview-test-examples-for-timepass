package org.example;

import java.util.concurrent.CompletableFuture;

public class CompleteableFutureDemo {

    public static void main(String[] args) {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("worker");
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }
            return "ok";
        });
    }
}
