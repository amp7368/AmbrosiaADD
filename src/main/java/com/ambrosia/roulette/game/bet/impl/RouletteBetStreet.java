package com.ambrosia.roulette.game.bet.impl;

import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.bet.types.RouletteBetFactory;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.RoulettePartialBet;
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
    private static String shortDescriptionStreet(RouletteStreet street, boolean bold) {
        return Arrays.stream(street.getSpaces())
            .map(space -> space.display(bold, bold))
            .collect(Collectors.joining(", "));
    }

    @Override
    protected String shortDescription(boolean bold) {
        return "(" + streets().stream()
            .map(street -> shortDescriptionStreet(street, bold))
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
        return Arrays.stream(this.streets).anyMatch(street -> streetHasRoll(roll, street));
    }

    private boolean streetHasRoll(int roll, RouletteStreet street) {
        return Arrays.stream(street.getSpaces())
            .anyMatch(space -> space.digit() == roll);
    }
}
