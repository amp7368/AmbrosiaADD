package com.ambrosia.add.discord.log;

import apple.utilities.util.Pretty;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.operation.OperationEntity;
import com.ambrosia.add.discord.DiscordConfig;
import com.ambrosia.add.discord.DiscordModule;
import java.awt.Color;
import java.time.Instant;
import lib.DCF;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class DiscordLog {

    private static DiscordLog instance;
    private DCF dcf;
    private final TextChannel channel;

    public DiscordLog(DCF dcf) {
        this.dcf = dcf;
        channel = dcf.jda().getTextChannelById(DiscordConfig.get().logChannel);
        instance = this;
    }

    public static DiscordLog log() {
        return instance;
    }

    public void modifyDiscord(ClientEntity client, User actor) {
        EmbedBuilder msg = normal("Modify Discord", actor);
        client(msg, client).setDescription(client.discord.fullName()).setThumbnail(client.discord.avatarUrl);
        log(msg.build());
    }

    public void modifyMinecraft(ClientEntity client, User actor) {
        EmbedBuilder msg = normal("Modify Minecraft", actor);
        client(msg, client).setDescription(client.minecraft.name).setThumbnail(client.minecraft.skinUrl());
        log(msg.build());
    }

    public void createAccount(ClientEntity client, User actor) {
        EmbedBuilder msg = constructive("Create Account", actor);
        client(msg, client);
        log(msg.build());
    }

    public void operation(ClientEntity client, OperationEntity operation, User actor) {
        EmbedBuilder msg = embed(operation.display(), actor).setColor(operation.changeAmount < 0 ? 0xfc5603 : 0x9dfc03);

        client(msg, client).addBlankField(true).addField(String.format("Id: #%d", operation.id), "", true);
        log(msg.build());
    }

    private void log(MessageEmbed msg) {
        DiscordModule.get().logger().info(msg.toData());
        if (channel != null) channel.sendMessageEmbeds(msg).queue();
    }

    private EmbedBuilder client(EmbedBuilder msg, ClientEntity client) {
        msg.setAuthor(String.format("%s (#%d)", client.displayName, client.uuid));
        msg.addField("Credits", Pretty.commas(client.credits), true);
        return msg;
    }

    private EmbedBuilder constructive(String title, User actor) {
        return embed(title, actor).setColor(Color.GREEN);
    }

    private EmbedBuilder destructive(String title, User actor) {
        return embed(title, actor).setColor(Color.RED);
    }

    private EmbedBuilder normal(String title, User actor) {
        return embed(title, actor).setColor(Color.CYAN);
    }

    private EmbedBuilder embed(String title, User actor) {
        return new EmbedBuilder().setTitle(title).setFooter(actor.getAsTag(), actor.getAvatarUrl()).setTimestamp(Instant.now());
    }
}
