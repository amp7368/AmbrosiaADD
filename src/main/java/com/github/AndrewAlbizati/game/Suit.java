package com.github.AndrewAlbizati.game;

/**
 * Represents the different playing card suits.
 * SPADE, HEART, DIAMOND, CLUB
 */
public enum Suit {
    SPADE("\u2660"), HEART("\u2665"), DIAMOND("\u2666"), CLUB("\u2663");

    public final String character;
    Suit(String character) {
        this.character = character;
    }
}
