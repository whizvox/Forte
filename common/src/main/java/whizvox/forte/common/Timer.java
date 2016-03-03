package whizvox.forte.common;

public abstract class Timer implements Runnable {

    private long lastTick;
    private boolean shouldStop = false;
    private int limit;

    public Timer(int capRate) {
        setCap(capRate);
    }

    @Override
    public final void run() {
        begin();
        lastTick = System.currentTimeMillis();
        try {
            while (!shouldStop) {
                final int overflow = (int) ((System.currentTimeMillis() - lastTick) - limit);
                if (overflow >= 0) {
                    int sleepTime = limit - overflow;
                    if (sleepTime > 0) {
                        loop();
                        Thread.sleep(sleepTime);
                    }
                    lastTick = System.currentTimeMillis();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        end();
    }

    public void setShouldStop(boolean shouldStop) {
        this.shouldStop = shouldStop;
    }

    public final void setCap(int cap) {
        if (cap > 0 && cap <= 1000) {
            this.limit = 1000 / cap;
        } else {
            throw new IllegalArgumentException("Invalid cap: " + cap);
        }
    }

    public abstract void begin();

    public abstract void loop();

    public abstract void end();

    public static void start(Timer timer) {
        new Thread(timer).start();
    }

}
