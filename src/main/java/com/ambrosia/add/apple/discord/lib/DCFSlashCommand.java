package com.ambrosia.add.apple.discord.lib;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class DCFSlashCommand {


    public abstract CommandData getData();
    public void onCommand(SlashCommandInteractionEvent event) {

    }
}
