package com.ambrosia.roulette.game.bet.impl.outside;

import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.RoulettePartialBet;

public class RouletteBetDozen extends RouletteBet {

    private RouletteBetDozenType dozen;

    public RouletteBetDozen(RouletteBetType<?> type, RoulettePartialBet bet) {
        super(type, bet);
    }

    public RouletteBet finalizeDozen(RouletteBetDozenType dozen) {
        this.dozen = dozen;
        return this;
    }

    @Override
    protected String shortDescription(boolean bold) {
        return "Dozen (%s)".formatted(dozen.range());
    }

    @Override
    public boolean isWinningSpace(int roll) {
        return dozen.inRange(roll);
    }
}
