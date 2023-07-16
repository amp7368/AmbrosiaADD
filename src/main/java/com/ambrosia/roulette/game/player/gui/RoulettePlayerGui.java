package com.ambrosia.roulette.game.player.gui;

import com.ambrosia.roulette.game.player.RoulettePlayerGame;
import discord.util.dcf.DCF;
import discord.util.dcf.gui.base.GuiReplyFirstMessage;
import discord.util.dcf.gui.base.gui.DCFGui;

public class RoulettePlayerGui extends DCFGui {

    protected final RoulettePlayerGame player;

    public RoulettePlayerGui(DCF dcf, GuiReplyFirstMessage createFirstMessage, RoulettePlayerGame player) {
        super(dcf, createFirstMessage);
        this.player = player;
    }

    public RoulettePlayerGui recreate(GuiReplyFirstMessage reply) {
        RoulettePlayerGui gui = new RoulettePlayerGui(dcf, reply, player);
        this.pageMap.forEach(gui::addPage);
        this.subPages.forEach(gui::addSubPage);
        return gui;
    }

    public RoulettePlayerGame getPlayer() {
        return player;
    }
}
