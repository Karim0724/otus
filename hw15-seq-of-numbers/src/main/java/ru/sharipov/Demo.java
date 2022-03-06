package ru.sharipov;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        int[] value = new int[1];
        boolean[] changed = new boolean[1];
        Object lock = new Object();
        Thread first = new Thread(() -> {
            boolean ascending = true;
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (lock) {
                    while (changed[0]) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    sleep();
                    if (value[0] == 10) {
                        ascending = false;
                    }
                    if (value[0] == 1) {
                        ascending = true;
                    }
                    if (ascending) {
                        value[0]++;
                    } else {
                        value[0]--;
                    }
                    changed[0] = true;
                    System.out.println(Thread.currentThread().getName() + " : " + value[0]);
                    lock.notifyAll();
                }
            }
        });
        Thread second = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (lock) {
                        while (!changed[0]) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        sleep();
                        changed[0] = false;
                        System.out.println(Thread.currentThread().getName() + " : " + value[0]);
                        lock.notifyAll();
                    }
                }
            }
        });
        first.start();
        sleep();
        second.start();
        first.join();
        second.join();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
