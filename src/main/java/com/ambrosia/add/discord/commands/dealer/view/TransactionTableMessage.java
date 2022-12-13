package com.ambrosia.add.discord.commands.dealer.view;

import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class TransactionTableMessage extends DCFGuiPage<DCFGui> {

    public TransactionTableMessage(DCFGui gui, long startAt) {
        super(gui);
    }

    @Override
    public MessageCreateData makeMessage() {
        return MessageCreateData.fromContent("Hey :D");
    }
}
