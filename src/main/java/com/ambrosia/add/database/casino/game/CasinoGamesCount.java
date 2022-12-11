package com.ambrosia.add.database.casino.game;

import com.ambrosia.add.database.casino.GameResultAggregate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CasinoGamesCount {

    public final Map<String, CasinoGamesCountByName> gamesByName = new HashMap<>();
    public long count = 0;
    public long deltaWinnings = 0;

    public CasinoGamesCount(List<GameResultAggregate> query) {
        for (GameResultAggregate queryEntry : query) {
            this.put(queryEntry);
        }
    }

    public void put(GameResultAggregate queryEntry) {
        CasinoGamesCountByName byGame = this.gamesByName.computeIfAbsent(queryEntry.name, CasinoGamesCountByName::new);
        this.count += queryEntry.count;
        this.deltaWinnings += queryEntry.deltaWinnings;
        byGame.put(queryEntry);
    }
}
