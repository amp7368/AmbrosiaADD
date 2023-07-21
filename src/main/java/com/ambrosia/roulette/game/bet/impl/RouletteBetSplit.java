package com.ambrosia.roulette.game.bet.impl;

import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.RoulettePartialBet;

public class RouletteBetSplit extends RouletteBetBasket {

    public RouletteBetSplit(RouletteBetType<?> typeId, RoulettePartialBet bet) {
        super(typeId, bet, 2);
    }
}
