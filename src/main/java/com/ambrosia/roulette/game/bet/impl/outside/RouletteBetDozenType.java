package com.ambrosia.roulette.game.bet.impl.outside;

public enum RouletteBetDozenType {
    LOW(1, 12),
    MID(13, 24),
    HIGH(25, 36);

    private final int lower;
    private final int upper;

    RouletteBetDozenType(int lower, int upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public boolean inRange(int roll) {
        return this.lower <= roll && roll <= this.upper;
    }

    public String range() {
        return "%d - %d".formatted(lower, upper);
    }

    public int id() {
        return this.upper / 12;
    }
}
