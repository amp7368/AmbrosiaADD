package com.ambrosia.add.database.casino.game;

import java.util.Map;

public record CasinoGamesCount(Map<String, CasinoGamesCountByName> gamesByName) {

}
