package com.ambrosia.roulette.game.bet.types;

import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.RouletteBetPart;
import com.ambrosia.roulette.game.player.RoulettePartialBet;
import com.ambrosia.roulette.table.RouletteSpace;
import com.ambrosia.roulette.table.RouletteTable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

    public List<RouletteBetPart> actions() {
        // get all the allowed spaces of a bet
        boolean isRequire0InBet = type == RouletteBetType.TRIO;
        RouletteTable table = Roulette.TABLE;
        List<RouletteSpace> allowedSpaces = isRequire0InBet ? table.zeroStreet(true) : table.spaces(true);

        // deal with trivial cases
        if (basket.isEmpty()) return allowedSpaces.stream().map(RouletteSpace::betPart).toList();
        if (this.countOfJoin == basket.size()) return Collections.emptyList();

        // filter to only spaces next
        return allowedSpaces.stream().filter(this::isNeighbor).map(RouletteSpace::betPart).toList();
    }

    @Override
    protected String shortDescription() {
        return this.basket.stream()
            .map(s -> s.display(true, true))
            .collect(Collectors.joining(", "));
    }

    public boolean isNeighbor(RouletteSpace space) {
        return basket.stream().allMatch(space::isNeighbor);
    }

    public List<RouletteBetPart> partList() {
        return basket.stream().map(RouletteSpace::betPart).toList();
    }

    public boolean isComplete() {
        return basket.size() == countOfJoin;
    }

    @Override
    public boolean isWinningSpace(int roll) {
        return this.basket.contains(Roulette.TABLE.getSpace(roll));
    }
}
