package com.ambrosia.roulette.game.table.gui;

public class EditOnTimer {

    private final Runnable callback;
    private final long interval;
    private long lastUpdated;
    private boolean isQueued;

    public EditOnTimer(Runnable callback, long interval) {
        this.callback = callback;
        this.interval = interval;
    }

    public void tryRun() {
        synchronized (this) {
            if (this.isQueued) return;
            this.isQueued = true;
            new Thread(this::callback).start();
        }
    }

    private void callback() {
        try {
            long sleep = getSleep();
            if (sleep > 0)
                Thread.sleep(sleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (this) {
            this.lastUpdated = System.currentTimeMillis();
            this.isQueued = false;
        }
        this.callback.run();
    }

    private long getSleep() {
        synchronized (this) {
            return interval - (System.currentTimeMillis() - this.lastUpdated);
        }
    }
}
