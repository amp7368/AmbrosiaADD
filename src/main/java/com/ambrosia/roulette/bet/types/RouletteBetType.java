package com.ambrosia.roulette.bet.types;

import apple.utilities.gson.adapter.GsonEnumTypeHolder;
import apple.utilities.util.Pretty;
import java.util.function.Function;

public enum RouletteBetType implements GsonEnumTypeHolder<RouletteBet> {
    STRAIGHT_UP(35, RouletteBetBasket.class, RouletteBetBasket.factory(1)),
    SPLIT(17, RouletteBetBasket.class, RouletteBetBasket.factory(2)),
    STREET(11, RouletteBetStreet.class, RouletteBetStreet.factory(1)),
    TRIO(11, RouletteBetBasket.class, RouletteBetBasket.factory(3)),
    CORNER(8, RouletteBetBasket.class, RouletteBetBasket.factory(4)),
    LINE(5, RouletteBetStreet.class, RouletteBetStreet.factory(2)),
    COLUMN(2, null, null),
    DOZEN(2, null, null),
    LOW(1, null, null),
    HIGH(1, null, null),
    EVEN(1, null, null),
    ODD(1, null, null),
    RED(1, null, null),
    BLACK(1, null, null);

    private final double betMultiplier;
    private final Class<? extends RouletteBet> type;
    private final Function<RouletteBetType, ? extends RouletteBet> createFn;

    RouletteBetType(double betMultiplier, Class<? extends RouletteBet> type,
        Function<RouletteBetType, ? extends RouletteBet> createFn) {
        this.betMultiplier = betMultiplier;
        this.type = type;
        this.createFn = createFn;
    }

    public String displayName() {
        return Pretty.spaceEnumWords(name());
    }

    public String getTypeId() {
        return name().toLowerCase();
    }

    public double betMultiplier() {
        return this.betMultiplier;
    }

    @Override
    public Class<? extends RouletteBet> getTypeClass() {
        return this.type;
    }

    public RouletteBet create() {
        return createFn.apply(this);
    }
}
