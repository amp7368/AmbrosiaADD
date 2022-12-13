package com.ambrosia.add.discord.commands.player.request;

import com.ambrosia.add.discord.util.BaseCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandRequest extends BaseCommand {

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {

    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("request", "Request to withdraw/deposit emeralds");
    }
}
