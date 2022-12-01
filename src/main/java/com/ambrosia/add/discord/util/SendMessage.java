package com.ambrosia.add.discord.util;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.discord.DiscordModule;
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

    default MessageEmbed missingOption(String option) {
        return error(String.format("'%s' is required", option));
    }

    default void missingOption(SlashCommandInteractionEvent event, String option) {
        event.replyEmbeds(missingOption(option)).queue();
    }

    default MessageEmbed embedClientProfile(ClientEntity client) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(client.displayName);
        if (client.discord != null) {
            embed.setAuthor(client.discord.fullName(), null, client.discord.avatarUrl);
        }
        if (client.minecraft != null) {
            embed.setFooter(client.minecraft.name, DiscordModule.AMBROSIA_ICON);
            embed.setThumbnail(client.minecraft.skinUrl());
        } else embed.setFooter(null, DiscordModule.AMBROSIA_ICON);
        embed.setTimestamp(client.dateCreated.toInstant());
        embed.addField("Credits", String.valueOf(client.credits), true);
        embed.setDescription("idk what else to put here");
        return embed.build();
    }
}
