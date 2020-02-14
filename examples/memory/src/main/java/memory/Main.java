package memory;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("---------------------\nCache miss penalty\n---------------------");
        exampleCacheMissPenalty();
        System.out.println();
        System.out.println("---------------------\nFalse sharing penalty\n---------------------");
        exampleFalseSharing();
        System.out.println();
        System.out.println("---------------------\nLocality penaly\n---------------------");
        exampleLocalityPenalty();
    }

    private static void exampleCacheMissPenalty() {
        int x = 100;

        // Access time for our 'x' variable should be pretty slow now.
        long start = System.nanoTime();
        System.out.println("x: " + x);
        long end = System.nanoTime();

        System.out.println("Execution time with cache miss: " + (end-start) + "ns");

        // Access time for our 'x' variable should be faster now, 'x' lives in the cache.
        start = System.nanoTime();
        System.out.println("x: " + x);
        end = System.nanoTime();

        System.out.println("Execution time with cache hit: " + (end-start) + "ns");
    }

    private static void exampleFalseSharing() throws InterruptedException {
        final int[] fill = new int[100000000];

        for (int i=0; i < 100000000 ;i++) {
            fill[i] = i;
        }

        Thread a = new Thread(() -> {
            long sum = 0;

            for (int i=0; i < 100000000 ;i++) {
                sum += fill[i];
            }

            System.out.println("Sum: " + sum);
        });

        Thread b = new Thread(() -> {
            for (int i=0; i < 100000000 ;i++) {
                if (i % 10 == 0) {
                    fill[i] = fill[i] / 10;
                }
            }
        });

        long start = System.currentTimeMillis();
        a.start();
        b.start();
        a.join();
        long end = System.currentTimeMillis();

        System.out.println("Execution time with false sharing: " + (end-start) + "ms");

        fillCacheWithBogus();

        Thread c = new Thread(() -> {
            long sum = 0;

            for (int i=0; i < 100000000 ;i++) {
                sum += fill[i];
            }

            System.out.println("Sum: " + sum);
        });

        start = System.currentTimeMillis();
        c.start();
        c.join();
        end = System.currentTimeMillis();

        a.join();
        System.out.println("Execution time without false sharing: " + (end-start) + "ms");
    }

    public static void exampleLocalityPenalty() {
        int[][] a = new int[10000][10000];

        for (int i=0; i < 10000 ;i++) {
            for (int j=0; j < 10000 ;j++) {
                a[i][j] = i+j;
            }
        }

        fillCacheWithBogus();

        long sum = 0;

        // Bad locality
        long start = System.currentTimeMillis();
        for (int i=0; i < 10000 ;i++) {
            for (int j=0; j < 10000 ;j++) {
                sum += a[j][i];
            }
        }
        long end = System.currentTimeMillis();

        System.out.println("[Bad locality] sum: " + sum + ", calculated in " + (end-start) + "ms");

        sum = 0;

        fillCacheWithBogus();

        // Good locality
        start = System.currentTimeMillis();
        for (int i=0; i < 10000 ;i++) {
            for (int j=0; j < 10000 ;j++) {
                sum += a[i][j];
            }
        }
        end = System.currentTimeMillis();

        System.out.println("[Good locality] sum: " + sum + ", calculated in " + (end-start) + "ms");
    }

    public static void fillCacheWithBogus() {
        System.out.print("--> filling cache with bogus [");

        // 10 million integers should be enough to fill the cache with bogus
        int[] bogus = new int[10000000];

        for (int i=0; i < 10000000 ;i++) {
            bogus[i] = i;
        }

        for (int i=0; i < 10000000 ;i++) {
            if (bogus[i] != 0 && bogus[i] % 1000000 == 0) {
                System.out.print((bogus[i] / 100000) + "% ");
            }
        }

        System.out.println("100%]");
    }
}
