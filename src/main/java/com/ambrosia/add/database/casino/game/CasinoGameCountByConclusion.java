package com.ambrosia.add.database.casino.game;

public record CasinoGameCountByConclusion(long count, long deltaWinnings) {

    public long deltaWinningsAbs() {
        return Math.abs(deltaWinnings);
    }
}
