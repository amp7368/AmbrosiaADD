package com.ambrosia.roulette.img;

import java.security.SecureRandom;

public class RouletteImage {

    public static final String IMAGE_BETTING_TABLE = "https://static.voltskiya.com/ambrosia/casino/assets/BettingTable.2.jpg";
    public static final String IMAGE_STARTING_WHEEL = "https://static.voltskiya.com/ambrosia/casino/assets/StartingWheel.png";
    private static final SecureRandom random = new SecureRandom();

    public static SpinImage spin(int spinResult) {
        int version;
        synchronized (random) {
            version = random.nextInt(10);
        }
        return spin("default", spinResult, version);
    }

    public static SpinImage spin(String iteration, int num, int version) {
        String domain = "static.voltskiya.com/ambrosia/casino/wheel";
        String gif = String.format("https://%s/%s/gif/%s-%02d.%d.gif", domain, iteration, iteration, num, version);
        String last = String.format("https://%s/%s/last/%s-%02d.%d.png", domain, iteration, iteration, num, version);
        return new SpinImage(gif, last);
    }

    public record SpinImage(String gif, String last) {

    }
}
