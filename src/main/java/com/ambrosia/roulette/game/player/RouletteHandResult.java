package com.ambrosia.roulette.game.player;

import com.ambrosia.add.database.game.HandResult;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;

public class RouletteHandResult implements HandResult {

    private final String resultName;
    private final double betMultiplier;

    private RouletteHandResult(String resultName, double betMultiplier) {
        this.resultName = resultName;
        this.betMultiplier = betMultiplier;
    }

    public static RouletteHandResult win(RouletteBetType<?> type) {
        return new RouletteHandResult("WIN", type.betMultiplier());
    }

    public static RouletteHandResult loss(RouletteBetType<?> type) {
        return new RouletteHandResult(type.getTypeId(), -1);
    }

    @Override
    public double betMultiplier() {
        return this.betMultiplier;
    }

    @Override
    public String resultName() {
        return resultName;
    }
}
