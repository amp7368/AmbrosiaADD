package com.ambrosia.add.discord.view;

import discord.util.dcf.slash.DCFSlashCommand;
import discord.util.dcf.slash.DCFSlashSubCommand;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class ViewCommand extends DCFSlashCommand {

    @Override
    public SlashCommandData getData() {
        return Commands.slash("view", "View a table of transactions or profiles")
            .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public List<DCFSlashSubCommand> getSubCommands() {
        return List.of(new ViewTransactionsCommand());
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
    }
}
