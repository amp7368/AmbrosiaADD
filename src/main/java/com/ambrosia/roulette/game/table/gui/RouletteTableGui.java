package com.ambrosia.roulette.game.table.gui;

import com.ambrosia.roulette.game.game.RouletteGame;
import discord.util.dcf.DCF;
import discord.util.dcf.gui.base.GuiReplyFirstMessage;
import discord.util.dcf.gui.base.gui.DCFGui;

public class RouletteTableGui extends DCFGui {

    private final RouletteGame game;

    public RouletteTableGui(RouletteGame game, DCF dcf, GuiReplyFirstMessage createFirstMessage) {
        super(dcf, createFirstMessage);
        this.game = game;
    }

    public RouletteGame getGame() {
        return this.game;
    }
}
