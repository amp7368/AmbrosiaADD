package com.ambrosia.roulette.table;

import com.ambrosia.roulette.bet.RouletteBetPart;

public record RouletteSpace(RouletteSpaceColor isRed, int digit, RouletteBetPart betPart) {

    public RouletteSpace(boolean isRed, int digit) {
        this(isRed ? RouletteSpaceColor.RED : RouletteSpaceColor.BLACK, digit);
    }

    public RouletteSpace(RouletteSpaceColor color, int digit) {
        this(color, digit, new RouletteBetPart(digit));
    }

    public boolean isEven() {
        return digit % 2 == 0;
    }

    public boolean isNeighbor(RouletteSpace other) {
        if (other.digit == this.digit) return false;
        if (other.digit == 0) return this.isZeroStreet();
        if (digit == 0) return other.isZeroStreet();
        return Math.abs(other.digit - digit) == 1 || Math.abs(other.col() - this.col()) == 1;
    }

    private boolean isZeroStreet() {
        return this.digit <= 3;
    }

    private int col() {
        return digit % 3;
    }
}
