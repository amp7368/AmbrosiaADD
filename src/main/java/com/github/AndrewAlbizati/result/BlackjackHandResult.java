package com.github.AndrewAlbizati.result;

import com.ambrosia.add.database.game.HandResult;

public enum BlackjackHandResult implements HandResult {
    BUST(-1),
    WIN(1),
    PUSH(0),
    LOSE(-1),
    BLACKJACK(1.5);

    private final double betMultiplier;

    BlackjackHandResult(double betMultiplier) {
        this.betMultiplier = betMultiplier;
    }

    @Override
    public double betMultiplier() {
        return betMultiplier;
    }

    @Override
    public String resultName() {
        return name();
    }
}
