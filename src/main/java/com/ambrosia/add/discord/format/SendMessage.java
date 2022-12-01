package com.ambrosia.add.discord.format;

import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SendMessage {

    default EmbedBuilder success() {
        return new EmbedBuilder().setColor(Color.GREEN);
    }

    default MessageEmbed success(String msg) {
        return success().setDescription(msg).build();
    }

    default EmbedBuilder error() {
        return new EmbedBuilder().setColor(Color.RED);
    }

    default MessageEmbed error(String msg) {
        return error().setDescription(msg).build();
    }

    default MessageEmbed isNotDealer(SlashCommandInteractionEvent event) {
        return error(String.format("You must be a dealer to run '/%s'", event.getFullCommandName()));
    }
}
