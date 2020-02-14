package concurrency;

public class MyRunnable implements Runnable {

    public void run() {
        System.out.println("Current thread: " + Thread.currentThread().getName());
        System.out.println("Hello world!");
    }
}
