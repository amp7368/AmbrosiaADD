package com.ambrosia.roulette.table;

import java.util.Arrays;
import java.util.stream.Stream;

public class RouletteColumn {

    private final RouletteSpace[] column;

    public RouletteColumn(RouletteSpace[] column) {
        this.column = column;
    }

    public RouletteSpace[] spaces() {
        return column;
    }

    public Stream<RouletteSpace> spacesStream() {
        return Arrays.stream(column);
    }

}
