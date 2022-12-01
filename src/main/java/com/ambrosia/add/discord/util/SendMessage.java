package com.ambrosia.add.discord.util;

import com.ambrosia.add.database.client.ClientEntity;
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
        embed.setFooter("footer here", "https://cdn.discordapp.com/icons/923749890104885271/a_52da37c184005a14d15538cb62271b9b.webp");
        embed.setTimestamp(client.dateCreated.toInstant());
        embed.addField("Credits", String.valueOf(client.credits), true);
        embed.setThumbnail("https://cdn.discordapp.com/icons/923749890104885271/a_52da37c184005a14d15538cb62271b9b.webp");
        embed.setDescription("idk what else to put here");
        return embed.build();
    }
}
