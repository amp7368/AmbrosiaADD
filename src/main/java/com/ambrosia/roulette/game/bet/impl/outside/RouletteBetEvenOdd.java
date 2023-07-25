package com.ambrosia.roulette.game.bet.impl.outside;

import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.bet.types.RouletteBetFactory;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.RoulettePartialBet;
import com.ambrosia.roulette.table.RouletteSpace;

public class RouletteBetEvenOdd extends RouletteBet {

    private final boolean isEven;

    public RouletteBetEvenOdd(RouletteBetType<?> type, RoulettePartialBet bet, boolean isEven) {
        super(type, bet);
        this.isEven = isEven;
    }

    public static RouletteBetFactory<RouletteBetEvenOdd> factory(boolean isEven) {
        return (type, bet) -> new RouletteBetEvenOdd(type, bet, isEven);
    }

    @Override
    protected String shortDescription(boolean bold) {
        return this.isEven ? "Even" : "Odd";
    }

    @Override
    public boolean isWinningSpace(int roll) {
        RouletteSpace space = Roulette.TABLE.getSpace(roll);
        return this.isEven ? space.isEven() : space.isOdd();
    }
}
