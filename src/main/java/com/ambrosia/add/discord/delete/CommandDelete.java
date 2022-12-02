package com.ambrosia.add.discord.delete;

import com.ambrosia.add.discord.util.CommandBuilder;
import discord.util.dcf.slash.DCFSlashCommand;
import discord.util.dcf.slash.DCFSlashSubCommand;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandDelete extends DCFSlashCommand implements CommandBuilder {

    @Override
    public SlashCommandData getData() {
        SlashCommandData command = Commands.slash("delete", "Delete a log entry or profile");
        return command.setDefaultPermissions(DefaultMemberPermissions.ENABLED).setGuildOnly(true);
    }

    @Override
    public List<DCFSlashSubCommand> getSubCommands() {
        return List.of(new CommandDeleteOperation(), new CommandDeleteProfile());
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
    }
}
