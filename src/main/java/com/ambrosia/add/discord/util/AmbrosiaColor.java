package com.ambrosia.add.discord.util;

import java.awt.Color;

public class AmbrosiaColor {

    public static final int CASINO_COLOR = 0xe3790b;
    public static final int ERROR = 0x0;

    public static final int BAD = 0xfc5603;
    public static final int SUCCESS = Color.GREEN.getRGB();
    public static final int NORMAL = Color.CYAN.getRGB();

    public static class AmbrosiaColorRequest {

        public static final int CLAIMED = Color.YELLOW.getRGB();
        public static final int UNCLAIMED = Color.ORANGE.getRGB();
    }

    public static class AmbrosiaColorOperation {

        public static final int DEPOSIT = 0x9dfc03;
        public static int WITHDRAW = BAD;
    }
}
