package com.ambrosia.add.discord.util;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.operation.OperationReason;
import com.ambrosia.add.discord.DiscordModule;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.Nullable;

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
        return embedClientProfile(client, null);
    }

    default MessageEmbed embedClientProfile(ClientEntity client, @Nullable String titleExtra) {
        EmbedBuilder embed = new EmbedBuilder();
        String authorIcon;
        String authorName;
        if (client.discord != null) {
            authorName = client.discord.fullName();
            authorIcon = client.discord.avatarUrl;
        } else {
            authorName = null;
            authorIcon = DiscordModule.AMBROSIA_ICON;
        }
        if (titleExtra != null) {
            embed.setAuthor(titleExtra, null, authorIcon);
            embed.setDescription(authorName);
        } else {
            embed.setAuthor(authorName, null, authorIcon);
        }
        if (client.minecraft != null) {
            embed.setTitle(client.minecraft.name);
            embed.setFooter(client.displayName + " | Created", DiscordModule.AMBROSIA_ICON);
            embed.setThumbnail(client.minecraft.skinUrl());
        } else {
            embed.setTitle(client.displayName);
            embed.setFooter(" - | Created", DiscordModule.AMBROSIA_ICON);
        }
        embed.setTimestamp(client.dateCreated.toInstant());
        embed.addBlankField(false);
        embed.addField("Credits", Emeralds.longMessage(client.credits), false);
        embed.addField("Winnings", Emeralds.longMessage(client.total(OperationReason.WIN)), true);
        embed.addBlankField(true);
        embed.addField("Losses", Emeralds.longMessage(client.total(OperationReason.LOSS)), true);
        return embed.build();
    }
}
