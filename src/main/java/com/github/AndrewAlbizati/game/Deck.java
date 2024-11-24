package com.github.AndrewAlbizati.game;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents the deck in a game of Blackjack.
 */
public class Deck extends ArrayList<Card> {

    private final int amountOfDecks;
    private final SecureRandom random = new SecureRandom(); // todo make this use a seed soon

    public Deck(int amountOfDecks) {
        this.amountOfDecks = amountOfDecks;
        initializeDeck(amountOfDecks);
    }

    /**
     * Clears the deck and initializes it with new cards.
     *
     * @param amountOfDecks Number of decks of cards to be put together.
     */
    public void initializeDeck(int amountOfDecks) {
        clear();
        for (int deck = 0; deck < amountOfDecks; deck++) {
            // Add one deck
            for (int value = 0; value < 13; value++) {
                for (Suit suit : Suit.values()) {
                    add(new Card(value + 1, suit));
                }
            }
        }
    }

    /**
     * Shuffles the entire deck randomly.
     */
    public void shuffleDeck() {
        Collections.shuffle(this, random);
    }

    /**
     * Sorts the deck based on value, ignoring suits.
     */
    public void sortDeck() {
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < this.size() - 1; i++) {
                Card c = this.get(i);
                Card nextC = this.get(i + 1);
                if (c.compareTo(nextC) > 0) {
                    this.set(i, nextC);
                    this.set(i + 1, c);
                    sorted = false;
                }
            }
        }
    }

    /**
     * Removes the first card from the deck, reshuffles deck if empty.
     *
     * @return The first card from the deck
     */
    public Card deal() {
        if (size() > 0) {
            return remove(0);
        }
        if (amountOfDecks <= 0) {
            return null;
        }

        initializeDeck(amountOfDecks);
        shuffleDeck();

        return remove(0);
    }

    /**
     * Reverses the order of the entire deck.
     */
    public void reverseDeck() {
        Collections.reverse(this);
    }
}
