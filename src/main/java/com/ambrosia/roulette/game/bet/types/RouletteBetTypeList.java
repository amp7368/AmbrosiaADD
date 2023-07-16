package com.ambrosia.roulette.game.bet.types;

import apple.utilities.gson.adapter.GsonEnumTypeHolder;
import apple.utilities.util.Pretty;
import java.util.function.Function;

public enum RouletteBetTypeList implements GsonEnumTypeHolder<RouletteBetType> {
    STRAIGHT_UP(35, RouletteBetTypeBasket.class, RouletteBetTypeBasket.factory(1)),
    SPLIT(17, RouletteBetTypeBasket.class, RouletteBetTypeBasket.factory(2)),
    STREET(11, RouletteBetTypeStreet.class, RouletteBetTypeStreet.factory(1)),
    TRIO(11, RouletteBetTypeBasket.class, RouletteBetTypeBasket.factory(3)),
    CORNER(8, RouletteBetTypeBasket.class, RouletteBetTypeBasket.factory(4)),
    LINE(5, RouletteBetTypeStreet.class, RouletteBetTypeStreet.factory(2)),
    COLUMN(2, null, null),
    DOZEN(2, null, null),
    LOW(1, null, null),
    HIGH(1, null, null),
    EVEN(1, null, null),
    ODD(1, null, null),
    RED(1, null, null),
    BLACK(1, null, null);

    private final double betMultiplier;
    private final Class<? extends RouletteBetType> type;
    private final Function<RouletteBetTypeList, ? extends RouletteBetType> createFn;

    RouletteBetTypeList(double betMultiplier, Class<? extends RouletteBetType> type,
        Function<RouletteBetTypeList, ? extends RouletteBetType> createFn) {
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
    public Class<? extends RouletteBetType> getTypeClass() {
        return this.type;
    }

    public RouletteBetType create() {
        return createFn.apply(this);
    }
}
