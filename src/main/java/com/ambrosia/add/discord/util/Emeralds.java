package com.ambrosia.add.discord.util;

import apple.utilities.util.Pretty;

public class Emeralds {

    private static final int STACK = (int) Math.pow(64, 3);
    private static final int LIQUID = (int) Math.pow(64, 2);
    private static final int BLOCK = 64;

    public static String longMessage(long credits) {
        long creditsLeft = credits;
        long stx = creditsLeft / STACK;
        creditsLeft -= stx * STACK;
        long le = creditsLeft / LIQUID;
        creditsLeft -= le * LIQUID;
        long eb = creditsLeft / BLOCK;
        creditsLeft -= eb * BLOCK;
        long e = creditsLeft;

        StringBuilder message = new StringBuilder();
        if (stx != 0) append(message, stx, "stx");
        if (le != 0) append(message, le, "le");
        if (eb != 0) append(message, eb, "eb");
        if (e != 0) append(message, e, "e");
        return message.append(String.format("\n(%s total)", Pretty.commas(credits))).toString();
    }

    private static void append(StringBuilder message, long count, String unit) {
        if (count == 0) return;
        if (!message.isEmpty())
            message.append(", ");
        message.append(String.format("**%s** ", Pretty.commas(count))).append(unit);
    }
}
