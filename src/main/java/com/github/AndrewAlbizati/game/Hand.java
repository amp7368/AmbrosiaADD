package com.github.AndrewAlbizati.game;

import java.util.ArrayList;

/**
 * Represents a hand in Blackjack.
 */
public class Hand extends ArrayList<Card> {

    private boolean completed = false;

    /**
     * A hand is completed if the player has stood or if the player has busted.
     *
     * @return If the hand is completed.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * A hand is completed if the player has stood or if the player has busted.
     */
    public void setCompleted() {
        this.completed = true;
    }

    /**
     * Checks if a deck is soft (contains an ace valued at 11).
     *
     * @return Whether or not the deck is soft.
     */
    public boolean isSoft() {
        Deck d2 = new Deck(0);
        d2.addAll(this);

        d2.sortDeck();

        int aceCount = 0;
        for (Card c : this) {
            if (c.getValue() == 1) {
                aceCount++;
            }
        }
        // Hands without aces can't be soft
        if (aceCount == 0) {
            return false;
        }
        int scoreWithoutAce = 0;
        for (int i = 1; i < d2.size(); i++) {
            Card c = d2.get(i);
            switch (c.getValue()) {
                case 1 -> scoreWithoutAce += 1;
                case 11, 12, 13 -> scoreWithoutAce += 10;
                default -> scoreWithoutAce += c.getValue();
            }
        }

        return scoreWithoutAce < 11;
    }

    /**
     * Evaluates the score of the deck. Face cards are worth 10 credits, aces are worth 1 or 11 credits.
     *
     * @return The Blackjack score of the deck.
     */
    public int getScore() {
        int score = 0;

        Deck d2 = new Deck(0);
        d2.addAll(this);

        d2.sortDeck();
        d2.reverseDeck();

        boolean hasAce = false;

        for (Card c : d2) {
            switch (c.getValue()) {
                case 1 -> {
                    // Check if ace can be 11 later
                    hasAce = true;
                    score += 1;
                    break;
                }
                case 11, 12, 13 -> score += 10;
                default -> score += c.getValue();
            }
        }

        if (hasAce && score <= 11) { // there is no case where two aces are both 11
            score += 10;
        }

        return score;
    }

    /**
     * Converts a deck into a user-friendly string.
     *
     * @return A string that lists the name of each card in the deck.
     */
    public String toString() {
        StringBuilder deckString = new StringBuilder();
        for (int i = 0; i < this.size() - 1; i++) {
            deckString.append(this.get(i));
            deckString.append(" ");
        }
        deckString.append(this.get(this.size() - 1));

        return deckString.toString();
    }
}
