package com.ambrosia.roulette.table;

import com.ambrosia.roulette.game.bet.RouletteBetPart;

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

        int rowDiff = Math.abs(other.row() - row());
        int colDiff = Math.abs(other.col() - this.col());
        boolean isColNeighbor = colDiff == 1 && rowDiff == 0;
        boolean isRowNeighbor = colDiff == 0 && rowDiff == 1;
        return isRowNeighbor || isColNeighbor;
    }


    private boolean isZeroStreet() {
        return this.digit <= 3;
    }

    public int row() {
        if (digit == 0) return -1;
        return (digit - 1) / 3;
    }

    public int col() {
        if (digit == 0) return -1;
        return (digit - 1) % 3;
    }

    @Override
    public String toString() {
        return String.valueOf(this.digit);
    }

    public String display(boolean bold, boolean hashtag) {
        String msg = String.valueOf(this.digit);
        if (hashtag) msg = "#" + msg;
        if (bold) return "**%s**".formatted(msg);
        return msg;
    }
}
