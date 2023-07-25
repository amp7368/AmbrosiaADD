package com.ambrosia.roulette.table;

import apple.utilities.util.Pretty;

public enum RouletteSpaceColor {
    BLACK("\uD83D\uDD33"),
    RED("\uD83D\uDFE5"),
    GREEN("\uD83D\uDFE2");

    private final String emoji;

    RouletteSpaceColor(String emoji) {
        this.emoji = emoji;
    }

    public String displayName() {
        return Pretty.spaceEnumWords(name());
    }

    public String emoji() {
        return Pretty.spaceEnumWords(name());
    }
}
