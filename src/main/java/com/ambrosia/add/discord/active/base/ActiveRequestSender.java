package com.ambrosia.add.discord.active.base;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.discord.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class ActiveRequestSender {

    protected String username;
    protected String avatarUrl;
    protected long discordId;
    protected long client;

    public ActiveRequestSender(Member sender, ClientEntity client) {
        this(sender);
        this.client = client.id;
    }

    public ActiveRequestSender(Member sender) {
        this.username = sender.getUser().getName();
        this.avatarUrl = sender.getEffectiveAvatarUrl();
        this.discordId = sender.getIdLong();
    }

    public void author(EmbedBuilder embed) {
        embed.setAuthor(String.format("%s - (%s)", client().minecraft == null ? "NA" : client().minecraft.name, username), null,
            avatarUrl);
    }

    private ClientEntity client() {
        return ClientStorage.get().findById(client);
    }

    public void sendDm(MessageCreateData message) {
        DiscordBot.dcf.jda().openPrivateChannelById(this.discordId).queue((dm) -> {
            dm.sendMessage(message).queue();
        });
    }

    public void setClient(ClientEntity client) {
        this.client = client.id;
    }

}
