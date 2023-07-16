package com.ambrosia.add.discord.util;

import apple.utilities.util.Pretty;

public class Emeralds {

    private static final int STACK = (int) Math.pow(64, 3);
    private static final int LIQUID = (int) Math.pow(64, 2);
    private static final int BLOCK = 64;

    public static String longMessage(long credits) {
        return longMessage(credits, true);
    }

    public static String longMessage(long credits, boolean isBold) {
        return message(credits, Integer.MAX_VALUE, isBold) + String.format("\n(**%s** total)", Pretty.commas(credits));
    }

    public static String message(long credits, boolean isBold) {
        return message(credits, Integer.MAX_VALUE, isBold);
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
        if (stx != 0) truncate -= append(message, stx, "STX", truncate, isBold, false);
        if (le != 0) truncate -= append(message, le, "LE", truncate, isBold, false);
        if (eb != 0) truncate -= append(message, eb, "EB", truncate, isBold, false);
        if (e != 0) append(message, e, "E", truncate, isBold, true);
        return message.toString();
    }

    private static int append(StringBuilder message, long amount, String unit, int fieldsLeft, boolean isBold, boolean forceAdd) {
        if (!forceAdd && (amount == 0 || fieldsLeft == 0)) return fieldsLeft;
        if (!message.isEmpty()) message.append(", ");
        String format = isBold ? "**%s** " : "%s ";
        message.append(String.format(format, Pretty.commas(amount))).append(unit);
        return 1;
    }


    public static int leToEmeralds(int le) {
        return LIQUID * le;
    }
}
