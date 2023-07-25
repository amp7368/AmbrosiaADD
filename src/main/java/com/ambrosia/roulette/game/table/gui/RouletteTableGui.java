package com.ambrosia.roulette.game.table.gui;

import com.ambrosia.roulette.game.table.RouletteGame;
import com.ambrosia.roulette.game.table.RouletteGameManager;
import discord.util.dcf.DCF;
import discord.util.dcf.gui.base.GuiReplyFirstMessage;
import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.util.TimeMillis;

public class RouletteTableGui extends DCFGui {

    private final RouletteGame game;

    private final EditOnTimer timer = new EditOnTimer(this::editMessage, 1750);

    public RouletteTableGui(RouletteGame game, DCF dcf, GuiReplyFirstMessage createFirstMessage) {
        super(dcf, createFirstMessage);
        this.game = game;
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
}
