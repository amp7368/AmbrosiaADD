package com.ambrosia.roulette.game.bet.types;

import apple.utilities.util.Pretty;
import com.ambrosia.roulette.game.bet.impl.RouletteBetBasket;
import com.ambrosia.roulette.game.bet.impl.RouletteBetCorner;
import com.ambrosia.roulette.game.bet.impl.RouletteBetSixLine;
import com.ambrosia.roulette.game.bet.impl.RouletteBetSplit;
import com.ambrosia.roulette.game.bet.impl.RouletteBetStraightUp;
import com.ambrosia.roulette.game.bet.impl.RouletteBetStreet;
import com.ambrosia.roulette.game.bet.impl.outside.RouletteBetColor;
import com.ambrosia.roulette.game.bet.impl.outside.RouletteBetColumn;
import com.ambrosia.roulette.game.bet.impl.outside.RouletteBetDozen;
import com.ambrosia.roulette.game.bet.impl.outside.RouletteBetEvenOdd;
import com.ambrosia.roulette.game.bet.impl.outside.RouletteBetHighLow;
import com.ambrosia.roulette.game.player.RoulettePartialBet;
import com.ambrosia.roulette.table.RouletteSpaceColor;

public class RouletteBetType<Bet extends RouletteBet> {

    public static RouletteBetType<RouletteBetStraightUp> STRAIGHT_UP;
    public static RouletteBetType<RouletteBetSplit> SPLIT;
    public static RouletteBetType<RouletteBetStreet> STREET;
    public static RouletteBetType<RouletteBetBasket> TRIO;
    public static RouletteBetType<RouletteBetCorner> CORNER;
    public static RouletteBetType<RouletteBetSixLine> SIX_LINE;
    public static RouletteBetType<RouletteBetColumn> COLUMN;
    public static RouletteBetType<RouletteBetDozen> DOZEN;
    public static RouletteBetType<RouletteBetHighLow> LOW;
    public static RouletteBetType<RouletteBetHighLow> HIGH;
    public static RouletteBetType<RouletteBetEvenOdd> EVEN;
    public static RouletteBetType<RouletteBetEvenOdd> ODD;
    public static RouletteBetType<RouletteBetColor> RED;
    public static RouletteBetType<RouletteBetColor> BLACK;

    static {
        STRAIGHT_UP = new RouletteBetType<>("STRAIGHT_UP", 35, RouletteBetStraightUp.class, RouletteBetStraightUp::new);
        SPLIT = new RouletteBetType<>("SPLIT", 17, RouletteBetSplit.class, RouletteBetSplit::new);
        STREET = new RouletteBetType<>("STREET", 11, RouletteBetStreet.class, RouletteBetStreet.factory(1));
        TRIO = new RouletteBetType<>("TRIO", 11, RouletteBetBasket.class, RouletteBetBasket.factory(3));
        CORNER = new RouletteBetType<>("CORNER", 8, RouletteBetCorner.class, RouletteBetCorner::new);
        SIX_LINE = new RouletteBetType<>("SIX_LINE", 5, RouletteBetSixLine.class, RouletteBetSixLine::new);
        COLUMN = new RouletteBetType<>("COLUMN", 2, RouletteBetColumn.class, RouletteBetColumn::new);
        DOZEN = new RouletteBetType<>("DOZEN", 2, RouletteBetDozen.class, RouletteBetDozen::new);
        LOW = new RouletteBetType<>("LOW", 1, RouletteBetHighLow.class, RouletteBetHighLow.factory(false));
        HIGH = new RouletteBetType<>("HIGH", 1, RouletteBetHighLow.class, RouletteBetHighLow.factory(true));
        EVEN = new RouletteBetType<>("EVEN", 1, RouletteBetEvenOdd.class, RouletteBetEvenOdd.factory(true));
        ODD = new RouletteBetType<>("ODD", 1, RouletteBetEvenOdd.class, RouletteBetEvenOdd.factory(false));
        RED = new RouletteBetType<>("RED", 1, RouletteBetColor.class, RouletteBetColor.factory(RouletteSpaceColor.RED));
        BLACK = new RouletteBetType<>("BLACK", 1, RouletteBetColor.class, RouletteBetColor.factory(RouletteSpaceColor.BLACK));
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

    public Bet create(RoulettePartialBet bet) {
        return createFn.create(this, bet);
    }
}
