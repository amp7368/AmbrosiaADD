package com.ambrosia.roulette.game.bet.types;

import apple.utilities.util.Pretty;
import com.ambrosia.roulette.game.bet.impl.RouletteBetStraightUp;
import com.ambrosia.roulette.game.player.RoulettePartialBet;

public class RouletteBetType<Bet extends RouletteBet> {

    public static RouletteBetType<RouletteBetStraightUp> STRAIGHT_UP;
    public static RouletteBetType<RouletteBetBasket> SPLIT;
    public static RouletteBetType<RouletteBetStreet> STREET;
    public static RouletteBetType<RouletteBetBasket> TRIO;
    public static RouletteBetType<RouletteBetBasket> CORNER;
    public static RouletteBetType<RouletteBetStreet> LINE;
    public static RouletteBetType<RouletteBetBasket> COLUMN;
    public static RouletteBetType<RouletteBetBasket> DOZEN;
    public static RouletteBetType<RouletteBetBasket> LOW;
    public static RouletteBetType<RouletteBetBasket> HIGH;
    public static RouletteBetType<RouletteBetBasket> EVEN;
    public static RouletteBetType<RouletteBetBasket> ODD;
    public static RouletteBetType<RouletteBetBasket> RED;
    public static RouletteBetType<RouletteBetBasket> BLACK;

    static {
        STRAIGHT_UP = new RouletteBetType<>("STRAIGHT_UP", 35, RouletteBetStraightUp.class, RouletteBetStraightUp::new);
        SPLIT = new RouletteBetType<>("SPLIT", 17, RouletteBetBasket.class, RouletteBetBasket.factory(2));
        STREET = new RouletteBetType<>("STREET", 11, RouletteBetStreet.class, RouletteBetStreet.factory(1));
        TRIO = new RouletteBetType<>("TRIO", 11, RouletteBetBasket.class, RouletteBetBasket.factory(3));
        CORNER = new RouletteBetType<>("CORNER", 8, RouletteBetBasket.class, RouletteBetBasket.factory(4));
        LINE = new RouletteBetType<>("LINE", 5, RouletteBetStreet.class, RouletteBetStreet.factory(2));
        COLUMN = new RouletteBetType<>("COLUMN", 2, null, null);
        DOZEN = new RouletteBetType<>("DOZEN", 2, null, null);
        LOW = new RouletteBetType<>("LOW", 1, null, null);
        HIGH = new RouletteBetType<>("HIGH", 1, null, null);
        EVEN = new RouletteBetType<>("EVEN", 1, null, null);
        ODD = new RouletteBetType<>("ODD", 1, null, null);
        RED = new RouletteBetType<>("RED", 1, null, null);
        BLACK = new RouletteBetType<>("BLACK", 1, null, null);
    }

    private final String name;
    private final double betMultiplier;
    private final Class<? extends RouletteBet> type;
    private final RouletteBetFactory<Bet> createFn;

    public RouletteBetType(String name, double betMultiplier, Class<Bet> type, RouletteBetFactory<Bet> createFn) {
        this.name = name;
        this.betMultiplier = betMultiplier;
        this.type = type;
        this.createFn = createFn;
    }

    public String displayName() {
        return Pretty.spaceEnumWords(name);
    }

    public String getTypeId() {
        return name.toLowerCase();
    }

    public double betMultiplier() {
        return this.betMultiplier;
    }

    public Class<? extends RouletteBet> getTypeClass() {
        return this.type;
    }

    public Bet create(RoulettePartialBet bet) {
        return createFn.create(this, bet);
    }
}
