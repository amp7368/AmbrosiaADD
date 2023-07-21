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
            if (canUpdate()) {
                this.lastUpdated = System.currentTimeMillis();
            } else if (this.isQueued) {
                return;
            } else {
                this.isQueued = true;
            }
            new Thread(this::callback).start();
        }
    }

    private void callback() {
        synchronized (this) {
            try {
                Thread.sleep(System.currentTimeMillis() - this.lastUpdated);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.lastUpdated = System.currentTimeMillis();
            this.isQueued = false;
        }
        this.callback.run();
    }

    private boolean canUpdate() {
        return lastUpdated + interval < System.currentTimeMillis();
    }
}
