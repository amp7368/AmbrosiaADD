package com.ambrosia.roulette.game.player.gui.straight;

public enum RouletteStraightUpType {
    HIGH(2),
    MIDDLE(1),
    LOW(0);

    private final int column;

    RouletteStraightUpType(int column) {
        this.column = column;
    }

    public int getColumn() {
        return this.column;
    }
}
