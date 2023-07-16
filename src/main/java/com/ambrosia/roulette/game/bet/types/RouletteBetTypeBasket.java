package com.ambrosia.roulette.game.bet.types;

import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.RouletteBetPart;
import com.ambrosia.roulette.table.RouletteSpace;
import com.ambrosia.roulette.table.RouletteTable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class RouletteBetTypeBasket extends RouletteBetType {

    protected final Collection<RouletteSpace> basket = new ArrayList<>();
    protected final int countOfJoin;

    public RouletteBetTypeBasket(RouletteBetTypeList typeId, int countOfJoin) {
        super(typeId);
        this.countOfJoin = countOfJoin;
    }

    public static Function<RouletteBetTypeList, RouletteBetTypeBasket> factory(int countOfJoin) {
        return (type) -> new RouletteBetTypeBasket(type, countOfJoin);
    }

    @Override
    public List<RouletteBetPart> actions() {
        // get all the allowed spaces of a bet
        boolean isRequire0InBet = type == RouletteBetTypeList.TRIO;
        RouletteTable table = Roulette.TABLE;
        List<RouletteSpace> allowedSpaces = isRequire0InBet ? table.zeroStreet(true) : table.spaces(true);

        // deal with trivial cases
        if (basket.isEmpty()) return allowedSpaces.stream().map(RouletteSpace::betPart).toList();
        if (this.countOfJoin == basket.size()) return Collections.emptyList();

        // filter to only spaces next
        return allowedSpaces.stream().filter(this::isNeighbor).map(RouletteSpace::betPart).toList();
    }

    public boolean isNeighbor(RouletteSpace space) {
        return basket.stream().allMatch(space::isNeighbor);
    }

    @Override
    public List<RouletteBetPart> partList() {
        return basket.stream().map(RouletteSpace::betPart).toList();
    }

    @Override
    public boolean isComplete() {
        return basket.size() == countOfJoin;
    }

    @Override
    public boolean isWinningSpace(int roll) {
        return this.basket.contains(Roulette.TABLE.getSpace(roll));
    }
}
