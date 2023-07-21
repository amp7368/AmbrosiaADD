package com.ambrosia.roulette.game.bet.impl;

import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.RoulettePartialBet;

public class RouletteBetStraightUp extends RouletteBetBasket {

    public RouletteBetStraightUp(RouletteBetType<?> typeId, RoulettePartialBet bet) {
        super(typeId, bet, 1);
    }
}
