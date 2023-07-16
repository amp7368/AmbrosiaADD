package com.ambrosia.roulette.game.player;

import apple.utilities.util.Pretty;

public enum RoulettePlayerBetStatus {
    BETTING,
    WAITING;

    public String displayName() {
        return Pretty.spaceEnumWords(name());
    }
}
