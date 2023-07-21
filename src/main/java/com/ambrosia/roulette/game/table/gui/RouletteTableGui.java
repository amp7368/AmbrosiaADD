package com.ambrosia.roulette.game.table.gui;

import com.ambrosia.roulette.game.game.RouletteGame;
import discord.util.dcf.DCF;
import discord.util.dcf.gui.base.GuiReplyFirstMessage;
import discord.util.dcf.gui.base.gui.DCFGui;

public class RouletteTableGui extends DCFGui {

    private final RouletteGame game;

    private final EditOnTimer timer = new EditOnTimer(this::editMessage, 1750);

    public RouletteTableGui(RouletteGame game, DCF dcf, GuiReplyFirstMessage createFirstMessage) {
        super(dcf, createFirstMessage);
        this.game = game;
    }

    public RouletteGame getGame() {
        return this.game;
    }

    public void updateBetsUI() {
        timer.tryRun();
    }
}
