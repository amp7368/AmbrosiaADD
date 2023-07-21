package com.ambrosia.roulette.table;

import java.util.Arrays;
import java.util.stream.Stream;

public class RouletteColumn {

    private final RouletteSpace[] column;
    private final int id;

    public RouletteColumn(RouletteSpace[] column, int id) {
        this.column = column;
        this.id = id;
    }

    public RouletteSpace[] spaces() {
        return column;
    }

    public Stream<RouletteSpace> spacesStream() {
        return Arrays.stream(column);
    }

    public int getId() {
        return this.id;
    }
}
