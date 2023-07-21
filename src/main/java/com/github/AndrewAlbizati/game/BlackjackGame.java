package com.github.AndrewAlbizati.game;

import com.ambrosia.add.api.CreditReservation;
import com.ambrosia.add.database.game.GameBase;
import com.github.AndrewAlbizati.Blackjack;
import com.github.AndrewAlbizati.result.BlackjackGameResult;
import com.github.AndrewAlbizati.result.BlackjackHandResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game of Blackjack. Each user can only be playing 1 game.
 */
public class BlackjackGame extends GameBase {

    private final CreditReservation creditReservation;

    private final Hand dealerHand;
    private final List<Hand> playerHand = new ArrayList<>();
    private final Deck deck;
    private final BlackjackGameResult results;
    private int selectedHandIndex = 0;
    private boolean isDoubleDown;


    public BlackjackGame(int bet, CreditReservation creditReservation) {
        super(creditReservation);
        this.creditReservation = creditReservation;
        this.results = new BlackjackGameResult(bet);

        deck = new Deck(2);
        deck.shuffleDeck();

        playerHand.add(new Hand());
        dealerHand = new Hand();

        playerHand.get(0).add(getDeck().deal());
        dealerHand.add(getDeck().deal());

        playerHand.get(0).add(getDeck().deal());
        dealerHand.add(getDeck().deal());
    }

    @Override
    public String getName() {
        return Blackjack.GAME_NAME;
    }

    /**
     * Returns the index of the hand that the user is currently playing with. Only used if the user has split and has multiple active
     * hands.
     *
     * @return The index of the current hand.
     */
    public int getSelectedHandIndex() {
        return selectedHandIndex;
    }


    /**
     * Get the deck that is being used for this game of Blackjack.
     *
     * @return A deck object.
     */
    public Deck getDeck() {
        return this.deck;
    }

    /**
     * Get the hand that the dealer is playing with.
     *
     * @return A Hand object.
     */
    public Hand getDealerHand() {
        return dealerHand;
    }

    /**
     * Gets all of the player hands. Player can have more than one hand if they split.
     *
     * @return An ArrayList of Hands.
     */
    public List<Hand> getPlayerHands() {
        return playerHand;
    }

    public long getCurrentBet() {
        return results.getCurrentBet();
    }


    /**
     * Gets the amount of credits that a specific player has. Data stored in bjcredits.json
     *
     * @return The long value of the amount of credits the player has.
     */
    public long getPlayerTotalCredits() {
        return this.creditReservation.getClient().credits;
    }

    public void doubleDown() {
        this.isDoubleDown = true;
        this.results.multiplyBet(2);
    }

    public Hand getSelectedHand() {
        return this.playerHand.get(this.selectedHandIndex);
    }

    public boolean hasSplit() {
        return this.getPlayerHands().size() > 1;
    }

    public boolean isGameComplete() {
        if (getSelectedHand().isCompleted()) {
            if (getPlayerHands().size() > getSelectedHandIndex() + 1) {
                selectedHandIndex++;
            }
            return getSelectedHand().isCompleted();
        }
        return false;
    }

    public void setGameComplete() {
        getPlayerHands().forEach(Hand::setCompleted);
        selectedHandIndex = getPlayerHands().size() - 1;
    }

    public void setHandComplete() {
        getSelectedHand().setCompleted();
        selectedHandIndex = Math.min(selectedHandIndex + 1, getPlayerHands().size() - 1);
    }

    public void result(BlackjackHandResult handResult) {
        this.results.result(handResult);
    }

    @Override
    public void end() {
        this.setGameComplete();
        this.creditReservation.release(this.results.toEntity());
        super.end();
    }

    public BlackjackGameResult results() {
        return this.results;
    }

    public boolean canReserve(long askAmount) {
        return askAmount <= this.getPlayerTotalCredits();
    }
}
