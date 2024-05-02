package com.ambrosia.add.discord.log;

import apple.lib.modules.configs.data.config.AppleConfig;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.TimeFormat;

public class RestartingMessageManager {

    private static RestartingMessageManager instance;
    private static AppleConfig<RestartingMessageManager> config;
    private final Set<RestartingMessage> restartingMessages = new HashSet<>();

    public RestartingMessageManager() {
        instance = this;
    }

    public static RestartingMessageManager get() {
        return instance;
    }

    public static void setConfig(AppleConfig<RestartingMessageManager> config) {
        RestartingMessageManager.config = config;
    }

    public void load() {
        List<RestartingMessage> copy;
        synchronized (restartingMessages) {
            copy = List.copyOf(restartingMessages);
            restartingMessages.clear();
        }
        for (RestartingMessage message : copy) {
            message.editAndDelete();
        }
    }

    public void sendRestarting(MessageChannel channel) {
        String text = String.format("""
            Bot is restarting soon! Finish your current game, but please refrain from starting new games until
            1. This message is updated
            **OR**
            2. Time is up %s""", TimeFormat.RELATIVE.format(Instant.now().plus(5, ChronoUnit.MINUTES)));
        channel.sendMessageEmbeds(new EmbedBuilder().setDescription(text).build())
            .queue(this::saveRestartingMessage);
    }

    private void saveRestartingMessage(Message message) {
        synchronized (this.restartingMessages) {
            this.restartingMessages.add(new RestartingMessage(message.getChannel().getIdLong(), message.getIdLong()));
        }
        config.save();
    }

    public void removeRestarting(RestartingMessage message) {
        synchronized (this.restartingMessages) {
            this.restartingMessages.remove(message);
        }
    }
}
