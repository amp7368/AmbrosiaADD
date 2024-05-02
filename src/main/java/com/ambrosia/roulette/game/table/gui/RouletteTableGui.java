package com.ambrosia.roulette.game.table.gui;

import com.ambrosia.roulette.game.table.RouletteGame;
import com.ambrosia.roulette.game.table.RouletteGameManager;
import discord.util.dcf.DCF;
import discord.util.dcf.gui.base.GuiReplyFirstMessage;
import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.util.TimeMillis;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RouletteTableGui extends DCFGui {

    private static final Duration STALE_BETTING_DURATION = Duration.ofSeconds(60);
    private static final int LAST_BET_UPDATE_INTERVAL = 10_000;
    private final RouletteGame game;
    private final EditOnTimer timer = new EditOnTimer(this::editMessage, 1750);
    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);
    private ScheduledFuture<?> lastBetUpdateTask;
    private Instant lastBetInstant = Instant.now();

    public RouletteTableGui(RouletteGame game, DCF dcf, GuiReplyFirstMessage createFirstMessage) {
        super(dcf, createFirstMessage);
        this.game = game;
        this.lastBetUpdateTask = executor.schedule(() -> scheduleNextUpdate(true), LAST_BET_UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void scheduleNextUpdate(boolean shouldUpdate) {
        long millis = getUntilBettingStale().toMillis();
        millis %= LAST_BET_UPDATE_INTERVAL;
        if (millis - 100 < 0) millis += LAST_BET_UPDATE_INTERVAL;
        if (shouldUpdate) updateBetsUI();
        this.lastBetUpdateTask.cancel(false);
        this.lastBetUpdateTask = executor.schedule(() -> scheduleNextUpdate(true), millis, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getMillisToOld() {
        return TimeMillis.minToMillis(60);
    }

    @Override
    public void remove() {
        RouletteGameManager.removeGame(game.getId());
        super.remove();
    }

    public RouletteGame getGame() {
        return this.game;
    }

    public void updateBetsUI() {
        timer.tryRun();
    }

    public RouletteTableGui recreate(GuiReplyFirstMessage reply) {
        RouletteTableGui gui = new RouletteTableGui(this.game, this.dcf, reply);
        this.pageMap.forEach(gui::addPage);
        this.subPages.forEach(gui::addSubPage);
        return gui;
    }

    public void resetLastBetTimer() {
        synchronized (this.executor) {
            this.lastBetInstant = Instant.now();
        }
        scheduleNextUpdate(false);
    }

    public Duration getUntilBettingStale() {
        Duration sinceLastBet;
        synchronized (this.executor) {
            sinceLastBet = Duration.between(lastBetInstant, Instant.now());
        }
        return STALE_BETTING_DURATION.minus(sinceLastBet);
    }


    public long getUntilBettingStaleSeconds() {
        long millis = Math.max(0, getUntilBettingStale().toMillis());
        long mod = millis % 5000;
        millis -= mod;
        if (mod >= 2500) millis += 5000;
        return millis / 1000;
    }
}
