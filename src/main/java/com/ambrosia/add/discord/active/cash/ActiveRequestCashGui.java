package com.ambrosia.add.discord.active.cash;

import discord.util.dcf.gui.stored.DCFStoredGui;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class ActiveRequestCashGui extends DCFStoredGui {

    public ActiveRequestCashGui(long messageId) {
        super(messageId);
    }

    @Override
    protected MessageCreateData makeMessage() {
        return null;
    }

    @Override
    public void save() {

    }
}
