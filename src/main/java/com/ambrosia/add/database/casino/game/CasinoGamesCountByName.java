package com.ambrosia.add.database.casino.game;

import com.ambrosia.add.database.casino.GameResultAggregate;
import java.util.HashMap;
import java.util.Map;

public final class CasinoGamesCountByName {

    public final String name;
    public final Map<String, CasinoGameCountByConclusion> conclusionToGame = new HashMap<>();

    public int count = 0;
    public int deltaWinnings = 0;

    public CasinoGamesCountByName(String name) {
        this.name = name;
    }

    public void put(GameResultAggregate queryEntry) {
        count += queryEntry.count;
        deltaWinnings += queryEntry.deltaWinnings;
        conclusionToGame.put(queryEntry.conclusion, new CasinoGameCountByConclusion(queryEntry.count, queryEntry.deltaWinnings));
    }
}
