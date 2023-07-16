package com.ambrosia.roulette.game.bet.types;

import com.ambrosia.roulette.game.bet.RouletteBetPart;
import java.util.List;
import java.util.function.Function;

public class RouletteBetTypeStreet extends RouletteBetType {

    public RouletteBetTypeStreet(RouletteBetTypeList type) {
        super(type);
    }

    public static Function<RouletteBetTypeList, ? extends RouletteBetType> factory(int i) {
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
