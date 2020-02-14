package concurrency;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("---------------------\nThread creation\n---------------------");
        threadCreationExample();
        System.out.println("---------------------\nSleep\n---------------------");
        sleepExample();
        System.out.println("---------------------\nInterrupt\n---------------------");
        interruptExample();
        System.out.println("---------------------\nJoin\n---------------------");
        joinExample();
    }

    public static void threadCreationExample() throws InterruptedException {
        System.out.println("Current thread: " + Thread.currentThread().getName());

        Thread t = new Thread(() -> {
            System.out.println("Current thread: " + Thread.currentThread().getName());
            System.out.println("Hello world!");
        });

        Thread myRynnableThread = new Thread(new MyRunnable());
        Thread myThread = new MyThread();

        t.start();
        myRynnableThread.start();
        myThread.start();

        t.join();
        myRynnableThread.join();
        myThread.join();
    }

    public static void sleepExample() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("Current thread: " + Thread.currentThread().getName());
            try {
                for (int i = 0; i < 5; i++) {
                    System.out.println(i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t.start();
        t.join();
    }

    public static void interruptExample() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("Current thread: " + Thread.currentThread().getName());
            for (int i=0; i < 10000000 ;i++) {
                if (Thread.interrupted()) {
                    System.out.println(Thread.currentThread().getName() + " interrupted!");
                    return;
                }

                System.out.println(i);
            }
        });

        t.start();
        System.out.println("Current thread: " + Thread.currentThread().getName());
        t.interrupt();
        t.join();
    }

    public static void joinExample() {
        Thread t = new Thread(() -> {
            try {
                System.out.println("Current thread: " + Thread.currentThread().getName());
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Current thread: " + Thread.currentThread().getName());
        for (int i=0; i < 10 ;i++) {
            System.out.println(i);

            if (i == 5) {
                try {
                    t.start();
                    System.out.println("Waiting for joining thread...");
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
