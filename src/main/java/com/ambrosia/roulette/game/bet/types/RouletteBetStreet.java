package com.ambrosia.roulette.game.bet.types;

import com.ambrosia.roulette.game.player.RoulettePartialBet;
import com.ambrosia.roulette.table.RouletteSpace;
import com.ambrosia.roulette.table.RouletteStreet;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class RouletteBetStreet extends RouletteBet {

    private final int streetCount;
    private RouletteStreet[] streets;

    public RouletteBetStreet(RouletteBetType<?> type, RoulettePartialBet bet, int streetCount) {
        super(type, bet);
        this.streetCount = streetCount;
    }

    public static RouletteBetFactory<RouletteBetStreet> factory(int streets) {
        return (type, bet) -> new RouletteBetStreet(type, bet, streets);
    }

    @NotNull
    private static String shortDescriptionStreet(RouletteStreet street) {
        return Arrays.stream(street.getSpaces())
            .map(RouletteSpace::digit)
            .map(String::valueOf)
            .collect(Collectors.joining(", "));
    }

    @Override
    protected String shortDescription() {
        return "(" + streets().stream()
            .map(RouletteBetStreet::shortDescriptionStreet)
            .collect(Collectors.joining("), ("))
            + ")";
    }

    private List<RouletteStreet> streets() {
        return Arrays.stream(this.streets).sorted(RouletteStreet.COMPARATOR).toList();
    }

    public RouletteBet finalizeStreets(RouletteStreet... streets) {
        this.streets = streets;
        return this;
    }

    @Override
    public boolean isWinningSpace(int roll) {
        return false;
    }
}
