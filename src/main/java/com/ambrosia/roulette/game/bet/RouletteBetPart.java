package com.ambrosia.roulette.game.bet;

import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.table.RouletteSpace;

public class RouletteBetPart {

    private final int number;

    public RouletteBetPart(int number) {
        this.number = number;
    }

    public RouletteSpace space() {
        return Roulette.TABLE.getSpace(number);
    }
}
