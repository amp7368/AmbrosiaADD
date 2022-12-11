package com.ambrosia.add.discord.commands;

import com.ambrosia.add.discord.util.BaseCommand;
import com.github.AndrewAlbizati.command.BlackjackHelp;
import discord.util.dcf.slash.DCFSlashSubCommand;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandHelp extends BaseCommand {

    @Override
    public SlashCommandData getData() {
        return Commands.slash("help", "Various commands");
    }

    @Override
    public List<DCFSlashSubCommand> getSubCommands() {
        return List.of(new BlackjackHelp());
    }

    @Override
    public void onCheckedCommand(SlashCommandInteractionEvent event) {
    }
}
