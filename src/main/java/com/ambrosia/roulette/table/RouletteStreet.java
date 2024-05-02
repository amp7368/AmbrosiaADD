package com.ambrosia.roulette.table;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class RouletteStreet {

    public static final Comparator<RouletteStreet> COMPARATOR = Comparator.comparingInt(
        street -> street.getSpaces()[0].digit());
    private final RouletteSpace[] street;
    private final int streetNum;

    public RouletteStreet(int streetNum, RouletteSpace col1, RouletteSpace col2, RouletteSpace col3) {
        this.streetNum = streetNum;
        street = new RouletteSpace[]{col1, col2, col3};
    }

    public int getStreetNum() {
        return streetNum;
    }

    public RouletteSpace[] getSpaces() {
        return street;
    }

    public RouletteSpace getSpace(int column) {
        return this.street[column];
    }

    public String display() {
        return "(%s)".formatted(String.join(", ", spacesDisplay()));
    }

    private List<String> spacesDisplay() {
        synchronized (this.street) {
            return Arrays.stream(street).map(space -> space.display(false, false)).toList();
        }
    }

    public String id() {
        return String.join("_", spacesDisplay());
    }

    public boolean isNeighbor(RouletteStreet street) {
        int thisSpace = this.getSpace(0).digit();
        int otherSpace = street.getSpace(0).digit();
        int diff = Math.abs(thisSpace - otherSpace);
        return diff == 3;
    }
}
