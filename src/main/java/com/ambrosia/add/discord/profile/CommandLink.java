package com.ambrosia.add.discord.profile;

import java.util.List;
import discord.util.dcf.slash.DCFSlashCommand;
import discord.util.dcf.slash.DCFSlashSubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandLink extends DCFSlashCommand {

    @Override
    public SlashCommandData getData() {
        SlashCommandData command = Commands.slash("link", "Link minecraft and/or discord");
        return command.setDefaultPermissions(DefaultMemberPermissions.DISABLED).setGuildOnly(true);
    }

    @Override
    public List<DCFSlashSubCommand> getSubCommands() {
        return List.of(new CommandLinkDiscord(), new CommandLinkMinecraft());
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {

    }
}
