package whizvox.forte.common;

public abstract class Timer implements Runnable {

    private long lastTick;
    private boolean shouldStop = false;
    private int limit;

    public Timer(int capRate) {
        if (capRate > 0 && capRate <= 1000) {
            this.limit = 1000 / capRate;
        } else {
            throw new IllegalArgumentException("Invalid cap: " + capRate);
        }
    }

    @Override
    public final void run() {
        lastTick = System.currentTimeMillis();
        try {
            while (!shouldStop) {
                final int overflow = (int) ((System.currentTimeMillis() - lastTick) - limit);
                if (overflow >= 0) {
                    int sleepTime = limit - overflow;
                    if (sleepTime > 0) {
                        invoke();
                        Thread.sleep(sleepTime);
                    }
                    lastTick = System.currentTimeMillis();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setShouldStop(boolean shouldStop) {
        this.shouldStop = shouldStop;
    }

    public abstract void invoke();

    public static void start(Timer timer) {
        new Thread(timer).start();
    }

}
