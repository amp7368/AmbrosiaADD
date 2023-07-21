package com.ambrosia.roulette.table;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class RouletteTable {

    public static final int MAX_ROWS = 12;
    public static final int MAX_COLS = 3;

    private final RouletteStreet[] streets = new RouletteStreet[MAX_ROWS];
    private final RouletteColumn[] columns = new RouletteColumn[MAX_COLS];
    private final RouletteSpace[] spaces = new RouletteSpace[MAX_ROWS * MAX_COLS];
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
        this.columns[columnIndex] = new RouletteColumn(column, columnIndex);
    }

    private void createStreet(boolean red1, boolean red2, boolean red3, int streetNum) {
        int numIndex = streetNum * MAX_COLS;
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
        synchronized (this.streets) {
            return this.streets[street];
        }
    }

    public List<RouletteStreet> streets() {
        synchronized (this.streets) {
            return List.of(this.streets);
        }
    }

    public RouletteColumn getColumn(int column) {
        synchronized (this.columns) {
            return this.columns[column];
        }
    }

    public List<RouletteColumn> columns() {
        synchronized (this.columns) {
            return List.of(this.columns);
        }
    }
}