package com.ambrosia.roulette.game.bet.impl;

import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.bet.types.RouletteBetFactory;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.RoulettePartialBet;
import com.ambrosia.roulette.table.RouletteSpace;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class RouletteBetBasket extends RouletteBet {

    protected final Collection<RouletteSpace> basket = new ArrayList<>();
    protected final int countOfJoin;

    public RouletteBetBasket(RouletteBetType<?> typeId, RoulettePartialBet bet, int countOfJoin) {
        super(typeId, bet);
        this.countOfJoin = countOfJoin;
    }

    public static RouletteBetFactory<RouletteBetBasket> factory(int countOfJoin) {
        return (type, bet) -> new RouletteBetBasket(type, bet, countOfJoin);
    }

    public RouletteBet finalizeDigits(Collection<RouletteSpace> space) {
        this.basket.addAll(space);
        return this;
    }

    @Override
    protected String shortDescription(boolean bold) {
        return this.basket.stream()
            .map(s -> s.display(bold, bold))
            .collect(Collectors.joining(", "));
    }

    @Override
    public boolean isWinningSpace(int roll) {
        return this.basket.contains(Roulette.TABLE.getSpace(roll));
    }
}
