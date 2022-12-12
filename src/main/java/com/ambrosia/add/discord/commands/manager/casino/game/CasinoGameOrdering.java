package com.ambrosia.add.discord.commands.manager.casino.game;

import com.github.AndrewAlbizati.Blackjack;
import com.github.AndrewAlbizati.result.BlackjackHandResult;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CasinoGameOrdering {

    private static Map<String, Map<String, Integer>> ordering = new HashMap<>();

    static {
        Map<String, Integer> blackjack = new HashMap<>();
        ordering.put(Blackjack.GAME_NAME, blackjack);
        blackjack.put(BlackjackHandResult.BLACKJACK.resultName(), 0);
        blackjack.put(BlackjackHandResult.WIN.resultName(), 1);
        blackjack.put(BlackjackHandResult.SPLIT.resultName(), 2);
        blackjack.put(BlackjackHandResult.LOSE.resultName(), 3);
        blackjack.put(BlackjackHandResult.PUSH.resultName(), 4);
    }

    public synchronized static Comparator<String> comparing(String gameName) {
        Map<String, Integer> gameOrderingRaw = ordering.get(gameName.toUpperCase(Locale.ROOT));
        if (gameOrderingRaw == null) return String.CASE_INSENSITIVE_ORDER;
        Map<String, Integer> gameOrdering = new HashMap<>(gameOrderingRaw);
        return (s1, s2) -> {
            Integer i1 = gameOrdering.get(s2.toUpperCase(Locale.ROOT));
            Integer i2 = gameOrdering.get(s1.toUpperCase(Locale.ROOT));
            if (i1 == null && i2 == null) return s1.compareToIgnoreCase(s2);
            if (i1 == null) return 1;
            if (i2 == null) return 0;
            return i2 - i1;
        };
    }

}
