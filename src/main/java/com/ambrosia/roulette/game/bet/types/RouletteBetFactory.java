package com.ambrosia.roulette.game.bet.types;

import com.ambrosia.roulette.game.player.RoulettePartialBet;

@FunctionalInterface
public interface RouletteBetFactory<Bet extends RouletteBet> {

    Bet create(RouletteBetType type, RoulettePartialBet bet);
}
