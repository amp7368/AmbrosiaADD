package com.ambrosia.roulette.table;

public class RouletteColumn {

    private final RouletteSpace[] column;

    public RouletteColumn(RouletteSpace[] column) {
        this.column = column;
    }

    public RouletteSpace[] getSpaces() {
        return column;
    }

}
