package com.ambrosia.roulette.game.bet.impl.outside;

import com.ambrosia.roulette.Roulette;

public enum RouletteBetColumnType {
    ONE(0),
    TWO(1),
    THREE(2);

    private final int columnIndex;

    RouletteBetColumnType(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public boolean inRange(int roll) {
        int col = Roulette.TABLE.getSpace(roll).col();
        return this.columnIndex == col;
    }

    public String display() {
        return Roulette.TABLE.getColumn(columnIndex).display();
    }

    public int id() {
        return this.columnIndex;
    }
}
