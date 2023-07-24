package com.ambrosia.roulette.game.table;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.Nullable;

public class RouletteGameManager {


    private static final Map<Integer, RouletteGame> allGames = new HashMap<>();
    private static final Map<Long, RouletteGame> channelToGames = new HashMap<>();

    public static RouletteGame getGame(Channel channel) {
        synchronized (channelToGames) {
            return channelToGames.get(channel.getIdLong());
        }
    }

    public static RouletteGame createTable(TextChannel channel) {
        RouletteGame game;
        synchronized (allGames) {
            SecureRandom random = new SecureRandom();
            int id;
            do {
                id = random.nextInt(1, 10000);
            } while (allGames.containsKey(id));
            game = new RouletteGame(id, channel);
            allGames.put(id, game);
        }

        synchronized (channelToGames) {
            long channelId = channel.getIdLong();
            if (channelToGames.containsKey(channelId))
                throw new IllegalStateException("%s already has a session".formatted(channel.getName()));
            channelToGames.put(channelId, game);
        }
        return game;
    }

    public static void removeChannelGame(TextChannel channel) {
        synchronized (channelToGames) {
            channelToGames.remove(channel.getIdLong());
        }
    }

    public static void removeGame(int gameId) {
        synchronized (allGames) {
            allGames.remove(gameId);
        }
    }

    @Nullable
    public static RouletteGame getGame(int gameId) {
        synchronized (allGames) {
            return allGames.get(gameId);
        }
    }
}
