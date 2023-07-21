package com.ambrosia.roulette.table;

import java.util.Comparator;

public class RouletteStreet {

    public static final Comparator<RouletteStreet> COMPARATOR = Comparator.comparingInt(
        street -> street.getSpaces()[0].digit());
    private final RouletteSpace[] street;

    public RouletteStreet(RouletteSpace col1, RouletteSpace col2, RouletteSpace col3) {
        street = new RouletteSpace[]{col1, col2, col3};
    }

    public RouletteSpace[] getSpaces() {
        return street;
    }

    public RouletteSpace getSpace(int column) {
        return this.street[column];
    }
}
