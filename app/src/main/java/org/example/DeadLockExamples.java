package org.example;

class Pen {
    public synchronized void writeWithPenAndPaper(Paper paper) {
        System.out.println(Thread.currentThread().getName() + " locked Pen");
        sleep(500);
        System.out.println(Thread.currentThread().getName() + " waiting for Paper...");
        paper.finish();
    }

    public synchronized void finish() {
        System.out.println(Thread.currentThread().getName() + " finished via Pen");
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Paper {
    public synchronized void writeWithPaperAndPen(Pen pen) {
        System.out.println(Thread.currentThread().getName() + " locked Paper");
        sleep(500);
        System.out.println(Thread.currentThread().getName() + " waiting for Pen...");
        pen.finish();
    }

    public synchronized void finish() {
        System.out.println(Thread.currentThread().getName() + " finished via Paper");
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class DeadLockExamples {
    private static void runCase(String title, Runnable taskA, Runnable taskB) throws InterruptedException {
        System.out.println("\n=== " + title + " ===");
        Thread t1 = new Thread(taskA, title + "-T1");
        Thread t2 = new Thread(taskB, title + "-T2");

        t1.start();
        t2.start();

        t1.join(3000);
        t2.join(3000);

        if (t1.isAlive() && t2.isAlive()) {
            System.out.println("Deadlock likely happened: both threads are still blocked.");
        } else {
            System.out.println("No deadlock in this case/run.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Case 1: Runnable via lambda (interface), no separate task classes.
        Pen pen1 = new Pen();
        Paper paper1 = new Paper();
        boolean useFlag = true;
        Runnable lambdaTask1 = () -> {
            if(useFlag){
                synchronized (paper1){
                    pen1.writeWithPenAndPaper(paper1);
                }
            }else {
                pen1.writeWithPenAndPaper(paper1);
            }

        };
        Runnable lambdaTask2 = () -> {
            if(useFlag){
                synchronized (pen1){
                    paper1.writeWithPaperAndPen(pen1);
                }
            }else{
                paper1.writeWithPaperAndPen(pen1);
            }
        };
        runCase("CASE-1 Lambda Runnable", lambdaTask1, lambdaTask2);


    }
}
