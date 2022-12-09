package com.ambrosia.add.database.casino.game;

import com.ambrosia.add.database.casino.GameResultAggregate;
import java.util.HashMap;
import java.util.Map;

public final class CasinoGamesCountByName {

    private String name;
    private final Map<String, CasinoGameCountByConclusion> conclusionToGame = new HashMap<>();

    private int count;
    private int deltaWinnings;

    public CasinoGamesCountByName(String name) {
        this.name = name;
    }

    public void put(GameResultAggregate queryEntry) {
        count += queryEntry.count;
        deltaWinnings += queryEntry.deltaWinnings;
        conclusionToGame.put(queryEntry.conclusion, new CasinoGameCountByConclusion(queryEntry.count, queryEntry.deltaWinnings));
    }
}
