package com.ambrosia.add.discord.view;

import com.ambrosia.add.discord.util.CommandBuilder;
import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.slash.DCFSlashSubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class ViewTransactionsCommand extends DCFSlashSubCommand implements CommandBuilder {

    @Override
    public SubcommandData getData() {
        SubcommandData command = new SubcommandData("entry", "View a table of entries");
        command.addOption(OptionType.INTEGER, "id", "The id to start the table at");
        return command;
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        OptionMapping option = event.getOption("entry");
        long startAt = option == null ? 0 : option.getAsLong();
        DCFGui gui = new DCFGui(dcf, event::reply);
        gui.addPage(new TransactionTableMessage(gui, startAt)).send();
    }
}
