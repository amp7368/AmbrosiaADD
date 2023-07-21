package com.ambrosia.roulette.game.bet.impl;

import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.RoulettePartialBet;

public class RouletteBetSixLine extends RouletteBetStreet {

    public RouletteBetSixLine(RouletteBetType<?> type, RoulettePartialBet bet) {
        super(type, bet, 2);
    }
}
