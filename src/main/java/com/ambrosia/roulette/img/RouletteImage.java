package com.ambrosia.roulette.img;

import java.security.SecureRandom;

public class RouletteImage {

    public static final String IMAGE_BETTING_TABLE = "https://static.voltskiya.com/assets/BettingTable.jpg";
    public static final String IMAGE_STARTING_WHEEL = "https://static.voltskiya.com/assets/StartingWheel.png";
    private static final SecureRandom random = new SecureRandom();

    public static SpinImage spin(int spinResult) {
        int version;
        synchronized (random) {
            version = random.nextInt(10);
        }
        return spin(310, spinResult, version);
    }

    public static SpinImage spin(int iteration, int num, int version) {
        String domain = "static.voltskiya.com";
        String gif = String.format("https://%s/%d/gif/%d-%02d.%d.gif", domain, iteration, iteration, num, version);
        String last = String.format("https://%s/%d/last/%d-%02d.%d.png", domain, iteration, iteration, num, version);
        return new SpinImage(gif, last);
    }
}
