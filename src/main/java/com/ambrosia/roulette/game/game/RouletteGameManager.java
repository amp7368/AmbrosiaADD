package com.ambrosia.roulette.game.game;

import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class RouletteGameManager {


    private static final Map<Long, RouletteGame> games = new HashMap<>();

    public static RouletteGame getGame(long channel) {
        synchronized (games) {
            return games.get(channel);
        }
    }

    public static RouletteGame createSession(TextChannel channel) {
        RouletteGame game = new RouletteGame(channel);
        synchronized (games) {
            long channelId = channel.getIdLong();
            if (games.containsKey(channelId))
                throw new IllegalStateException("%s already has a session".formatted(channel.getName()));
            games.put(channelId, game);
        }
        return game;
    }
}
