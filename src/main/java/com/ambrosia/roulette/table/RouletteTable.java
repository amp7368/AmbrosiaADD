package com.ambrosia.roulette.table;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class RouletteTable {

    private final RouletteStreet[] streets = new RouletteStreet[12];
    private final RouletteColumn[] columns = new RouletteColumn[3];
    private final RouletteSpace[] spaces = new RouletteSpace[12 * 3];
    private final RouletteSpace zero = new RouletteSpace(RouletteSpaceColor.GREEN, 0);

    public RouletteTable() {
        int street = 0;
        // group1
        createStreet(true, false, true, street++);
        createStreet(false, true, false, street++);
        createStreet(true, false, true, street++);
        createStreet(false, false, true, street++);
        // group2
        createStreet(false, true, false, street++);
        createStreet(true, false, true, street++);
        createStreet(true, false, true, street++);
        createStreet(false, true, false, street++);
        // group3
        createStreet(true, false, true, street++);
        createStreet(false, false, true, street++);
        createStreet(false, true, false, street++);
        createStreet(true, false, true, street);

        setColumn(0);
        setColumn(1);
        setColumn(2);
    }

    private void setColumn(int columnIndex) {
        RouletteSpace[] column = new RouletteSpace[12];
        Arrays.setAll(column, i -> getStreet(i).getSpace(columnIndex));
        this.columns[columnIndex] = new RouletteColumn(column);
    }

    private void createStreet(boolean red1, boolean red2, boolean red3, int streetNum) {
        int numIndex = streetNum * 3;
        int num = numIndex + 1;
        RouletteSpace a = new RouletteSpace(red1, num++);
        RouletteSpace b = new RouletteSpace(red2, num++);
        RouletteSpace c = new RouletteSpace(red3, num);
        spaces[numIndex] = a;
        spaces[numIndex + 1] = b;
        spaces[numIndex + 2] = c;
        streets[streetNum] = new RouletteStreet(a, b, c);
    }

    public RouletteSpace getSpace(int roll) {
        return spaces[roll - 1];
    }

    public List<RouletteSpace> spaces(boolean includeZero) {
        return spacesStream(includeZero).toList();
    }

    public List<RouletteSpace> zeroStreet(boolean includeZero) {
        return zeroStreetStream(includeZero).toList();
    }

    public Stream<RouletteSpace> zeroStreetStream(boolean includeZero) {
        Stream<RouletteSpace> spaces;
        synchronized (this.spaces) {
            spaces = Arrays.stream(this.getStreet(0).getSpaces());
        }
        if (includeZero) return Stream.concat(spaces, Stream.of(zero));
        return spaces;
    }

    public Stream<RouletteSpace> spacesStream(boolean includeZero) {
        Stream<RouletteSpace> spaces;
        synchronized (this.spaces) {
            spaces = Arrays.stream(this.spaces);
        }
        if (includeZero) return Stream.concat(spaces, Stream.of(zero));
        return spaces;
    }

    public RouletteSpace getZero() {
        return zero;
    }

    public RouletteStreet getStreet(int street) {
        return this.streets[street];
    }

    public RouletteColumn getColumn(int column) {
        return this.columns[column];
    }
}