package com.ambrosia.add.discord.util;

import apple.utilities.util.Pretty;

public class Emeralds {

    private static final int STACK = (int) Math.pow(64, 3);
    private static final int LIQUID = (int) Math.pow(64, 2);
    private static final int BLOCK = 64;

    public static String longMessage(long credits) {
        return message(credits, Integer.MAX_VALUE, true) + String.format("\n(%s total)", Pretty.commas(credits));
    }

    public static String message(long credits, int truncate, boolean isBold) {
        long creditsLeft = credits;
        long stx = creditsLeft / STACK;
        creditsLeft -= stx * STACK;
        long le = creditsLeft / LIQUID;
        creditsLeft -= le * LIQUID;
        long eb = creditsLeft / BLOCK;
        creditsLeft -= eb * BLOCK;
        long e = creditsLeft;

        StringBuilder message = new StringBuilder();
        if (stx != 0) truncate -= append(message, stx, "stx", truncate, isBold);
        if (le != 0) truncate -= append(message, le, "le", truncate, isBold);
        if (eb != 0) truncate -= append(message, eb, "eb", truncate, isBold);
        if (e != 0) append(message, e, "e", truncate, isBold);
        return message.toString();
    }

    private static int append(StringBuilder message, long amount, String unit, int fieldsLeft, boolean isBold) {
        if (amount == 0 || fieldsLeft == 0) return 0;
        if (!message.isEmpty())
            message.append(", ");
        String format = isBold ? "**%s** " : "%s ";
        message.append(String.format(format, Pretty.commas(amount))).append(unit);
        return 1;
    }


}
