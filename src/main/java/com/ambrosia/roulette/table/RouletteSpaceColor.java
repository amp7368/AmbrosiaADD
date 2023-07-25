package com.ambrosia.roulette.table;

import apple.utilities.util.Pretty;

public enum RouletteSpaceColor {
    BLACK("\u26AB"),
    RED("\uD83D\uDD34"),
    GREEN("\uD83D\uDFE2");

    private final String emoji;

    RouletteSpaceColor(String emoji) {
        this.emoji = emoji;
    }

    public String displayName() {
        return Pretty.spaceEnumWords(name());
    }

    public String emoji() {
        return emoji;
    }
}
