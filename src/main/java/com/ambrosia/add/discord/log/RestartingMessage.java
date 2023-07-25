package com.ambrosia.add.discord.log;

import com.ambrosia.add.discord.DiscordBot;
import java.util.Objects;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

public class RestartingMessage {

    protected long channelId;
    protected long messageId;

    public RestartingMessage(long channelId, long messageId) {
        this.channelId = channelId;
        this.messageId = messageId;
    }

    public void editAndDelete() {
        TextChannel channel = null;
        try {
            channel = DiscordBot.dcf.jda().getTextChannelById(channelId);
        } catch (InsufficientPermissionException ignored) {
        }
        if (channel == null) RestartingMessageManager.get().removeRestarting(this);
        else channel.retrieveMessageById(messageId).queue(this::editAndDelete);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelId, messageId);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RestartingMessage other && other.messageId == this.messageId && other.channelId == this.channelId;
    }

    private void editAndDelete(Message message) {
        MessageEmbed original = message.getEmbeds().get(0);
        EmbedBuilder updated = new EmbedBuilder(original).setDescription(String.format("~~%s~~", original.getDescription()));

        MessageEditBuilder edit = new MessageEditBuilder().setEmbeds(updated.build());
        message.editMessage(edit.build()).queue();
        RestartingMessageManager.get().removeRestarting(this);
    }
}
