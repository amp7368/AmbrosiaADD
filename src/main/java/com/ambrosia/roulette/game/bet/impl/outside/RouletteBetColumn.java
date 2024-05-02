package com.ambrosia.roulette.game.bet.impl.outside;

import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.RoulettePartialBet;
import com.google.gson.Gson;
import java.util.Map;

public class RouletteBetColumn extends RouletteBet {

    private RouletteBetColumnType column;

    public RouletteBetColumn(RouletteBetType<?> type, RoulettePartialBet bet) {
        super(type, bet);
    }

    public RouletteBet finalizeColumn(RouletteBetColumnType column) {
        this.column = column;
        return this;
    }

    @Override
    protected String shortDescription(boolean bold) {
        return "Column (%s)".formatted(column.display());
    }

    @Override
    public boolean isWinningSpace(int roll) {
        return column.inRange(roll);
    }

    @Override
    public String toJson() {
        return new Gson().toJson(Map.of("column", column));
    }

}
