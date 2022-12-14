package com.github.AndrewAlbizati.game;

/**
 * Represents a standard playing card with a suit and value
 */
public class Card {

    private final int value;

    public int getValue() {
        return value;
    }

    private final Suit suit;

    public Suit getSuit() {
        return suit;
    }


    public Card(int value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    /**
     * Compares the values of the current card and an additional card
     *
     * @param card A seperate card to be compared.
     * @return 0 if the cards are equal, else it will return the difference
     */
    public int compareTo(Card card) {
        if (card == null) {
            return 0;
        }

        return this.value - card.value;
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Card other && other.value == this.value;
    }

    /**
     * Converts the card class instance into a human-readable name.
     *
     * @return The name of the card.
     */
    public String toString() {
        String num = switch (value) {
            case 1 -> "A";
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            default -> String.valueOf(value);
        };

        return String.format("[`%s %s`](https://github.com/amp7368/AmbrosiaADD/wiki/Blackjack)", suit.character, num);
    }
}
