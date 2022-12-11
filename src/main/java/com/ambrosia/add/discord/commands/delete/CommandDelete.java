package com.ambrosia.add.discord.commands.delete;

import com.ambrosia.add.discord.util.BaseCommand;
import discord.util.dcf.slash.DCFSlashSubCommand;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandDelete extends BaseCommand {

    @Override
    public SlashCommandData getData() {
        SlashCommandData command = Commands.slash("delete", "Delete a log entry or profile");
        return command.setDefaultPermissions(DefaultMemberPermissions.DISABLED).setGuildOnly(true);
    }

    @Override
    public boolean isOnlyManager() {
        return true;
    }

    @Override
    public List<DCFSlashSubCommand> getSubCommands() {
        return List.of(new CommandDeleteOperation(), new CommandDeleteProfile());
    }

    @Override
    public void onCheckedCommand(SlashCommandInteractionEvent event) {
    }
}
