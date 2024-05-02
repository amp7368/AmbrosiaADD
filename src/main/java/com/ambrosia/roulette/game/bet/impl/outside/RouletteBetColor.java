package com.ambrosia.roulette.game.bet.impl.outside;

import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.bet.types.RouletteBetFactory;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.RoulettePartialBet;
import com.ambrosia.roulette.table.RouletteSpaceColor;
import com.google.gson.Gson;
import java.util.Map;

public class RouletteBetColor extends RouletteBet {

    private final RouletteSpaceColor color;

    public RouletteBetColor(RouletteBetType<?> type, RoulettePartialBet bet, RouletteSpaceColor color) {
        super(type, bet);
        this.color = color;
    }

    public static RouletteBetFactory<RouletteBetColor> factory(RouletteSpaceColor color) {
        return (type, bet) -> new RouletteBetColor(type, bet, color);
    }

    @Override
    protected String shortDescription(boolean bold) {
        return color.displayName();
    }

    @Override
    public boolean isWinningSpace(int roll) {
        return Roulette.TABLE.getSpace(roll).color() == color;
    }

    @Override
    public String toJson() {
        return new Gson().toJson(Map.of("color", color));
    }
}
