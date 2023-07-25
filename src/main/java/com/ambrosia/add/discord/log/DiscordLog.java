package com.ambrosia.add.discord.log;

import apple.utilities.util.Pretty;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.operation.TransactionEntity;
import com.ambrosia.add.discord.DiscordConfig;
import com.ambrosia.add.discord.DiscordModule;
import com.ambrosia.add.discord.util.AmbrosiaColor;
import com.ambrosia.add.discord.util.AmbrosiaColor.AmbrosiaColorOperation;
import com.ambrosia.add.discord.util.SendMessage;
import discord.util.dcf.DCF;
import java.time.Instant;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class DiscordLog implements SendMessage {

    private static DiscordLog instance;
    private final DCF dcf;
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
        log(msg.build(), true);
    }

    public void modifyMinecraft(ClientEntity client, User actor) {
        EmbedBuilder msg = normal("Modify Minecraft", actor);
        client(msg, client).setDescription(client.minecraft.name).setThumbnail(client.minecraft.skinUrl());
        log(msg.build(), true);
    }

    public void createAccount(ClientEntity client, User actor) {
        EmbedBuilder msg = success("Create Account", actor);
        client(msg, client);
        log(msg.build(), true);
    }

    public void operation(ClientEntity client, TransactionEntity operation) {
        operation(client, operation, dcf.jda().getSelfUser(), false);
    }

    public void operation(ClientEntity client, TransactionEntity operation, User actor, boolean toDiscord) {
        int color = operation.changeAmount < 0 ? AmbrosiaColorOperation.WITHDRAW : AmbrosiaColorOperation.DEPOSIT;
        EmbedBuilder msg = embed(operation.display(), actor).setColor(color);
        client(msg, client).addBlankField(true).addField(String.format("Id: #%d", operation.id), "", true);
        log(msg.build(), toDiscord);
    }

    private void log(MessageEmbed msg, boolean toDiscord) {
        DiscordModule.get().logger().info(msg.toData());
        if (toDiscord && channel != null) channel.sendMessageEmbeds(msg).queue();
    }

    private EmbedBuilder client(EmbedBuilder msg, ClientEntity client) {
        msg.setAuthor(String.format("%s (#%d)", client.displayName, client.uuid));
        msg.addField("Credits", Pretty.commas(client.credits), true);
        return msg;
    }

    private EmbedBuilder success(String title, User actor) {
        return embed(title, actor).setColor(AmbrosiaColor.SUCCESS);
    }

    private EmbedBuilder normal(String title, User actor) {
        return embed(title, actor).setColor(AmbrosiaColor.NORMAL);
    }

    private EmbedBuilder embed(String title, User actor) {
        return new EmbedBuilder().setTitle(title).setFooter(actor.getName(), actor.getAvatarUrl()).setTimestamp(Instant.now());
    }
}
