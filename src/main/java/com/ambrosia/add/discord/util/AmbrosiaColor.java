package com.ambrosia.add.discord.util;

import java.awt.Color;

public class AmbrosiaColor {

    public static final int CASINO_COLOR = 0xe3790b;
    public static final int ERROR = 0x0;

    public static final int BAD = Color.RED.getRGB();
    public static final int SUCCESS = 0x02a302;
    public static final int NORMAL = Color.CYAN.getRGB();
    public static final int BLACKJACK = new Color(184, 0, 9).getRGB();

    public static class AmbrosiaColorRequest {

        public static final int CLAIMED = Color.YELLOW.getRGB();
        public static final int UNCLAIMED = Color.ORANGE.getRGB();
    }

    public static class AmbrosiaColorOperation {

        public static final int DEPOSIT = 0x9dfc03;
        public static int WITHDRAW = BAD;
    }

    public static class AmbrosiaColorGame {

        public static final int IN_PROGRESS = 0x6b6b6b;
        public static final int WIN = SUCCESS;
        public static final int TIE = Color.YELLOW.getRGB();
        public static final int LOSE = BAD;
        public static final int EXTRA_WIN = 0x00cf67;
    }
}
