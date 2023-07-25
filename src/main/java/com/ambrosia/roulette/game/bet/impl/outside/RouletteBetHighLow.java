package com.ambrosia.roulette.game.bet.impl.outside;

import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.bet.types.RouletteBetFactory;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.RoulettePartialBet;

public class RouletteBetHighLow extends RouletteBet {

    private final boolean isHigh;

    public RouletteBetHighLow(RouletteBetType<?> type, RoulettePartialBet bet, boolean isHigh) {
        super(type, bet);
        this.isHigh = isHigh;
    }

    public static RouletteBetFactory<RouletteBetHighLow> factory(boolean isHigh) {
        return (type, bet) -> new RouletteBetHighLow(type, bet, isHigh);
    }

    @Override
    protected String shortDescription(boolean bold) {
        return isHigh ? "High (19-36)" : "Low (1-18)";
    }

    @Override
    public boolean isWinningSpace(int roll) {
        if (isHigh) return roll > 18;
        return roll != 0 && roll <= 18;
    }
}
