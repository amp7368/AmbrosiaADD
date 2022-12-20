package com.ambrosia.roulette.bet.types;

import com.ambrosia.roulette.bet.RouletteBetPart;
import java.util.List;
import java.util.function.Function;

public class RouletteBetStreet extends RouletteBet {

    public RouletteBetStreet(RouletteBetType type) {
        super(type);
    }

    public static Function<RouletteBetType, ? extends RouletteBet> factory(int i) {
        return null;
    }

    @Override
    public List<RouletteBetPart> actions() {
        return null;
    }

    @Override
    public List<RouletteBetPart> partList() {
        return null;
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public boolean isWinningSpace(int roll) {
        return false;
    }
}
