package concurrency;

public class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println("Current thread: " + Thread.currentThread().getName());
        System.out.println("Hello world!");
    }
}
