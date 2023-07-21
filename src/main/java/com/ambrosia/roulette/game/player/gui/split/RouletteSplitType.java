package com.ambrosia.roulette.game.player.gui.split;

public enum RouletteSplitType {
    HIGH(2),
    MIDDLE(1),
    LOW(0);

    private final int column;

    RouletteSplitType(int column) {
        this.column = column;
    }

    public int getColumn() {
        return this.column;
    }
}
