package com.ambrosia.add.discord.active.base;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.discord.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class ActiveRequestSender {

    private String username;
    private String avatarUrl;
    private long discordId;
    private ClientEntity client;

    public ActiveRequestSender(Member sender, ClientEntity client) {
        this.username = sender.getUser().getAsTag();
        this.avatarUrl = sender.getEffectiveAvatarUrl();
        this.discordId = sender.getIdLong();
        this.client = client;
    }

    public ActiveRequestSender() {
    }

    public void author(EmbedBuilder embed) {
        embed.setAuthor(String.format("%s - (%s)", client.minecraft, username), null, avatarUrl);
    }

    public void sendDm(MessageCreateData message) {
        DiscordBot.dcf.jda().openPrivateChannelById(this.discordId).queue((dm) -> {
            dm.sendMessage(message).queue();
        });
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }
}
