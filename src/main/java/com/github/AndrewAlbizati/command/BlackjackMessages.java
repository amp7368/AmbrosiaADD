package com.github.AndrewAlbizati.command;

import com.ambrosia.add.discord.util.Emeralds;
import com.github.AndrewAlbizati.game.BlackjackGame;
import com.github.AndrewAlbizati.game.Hand;
import com.github.AndrewAlbizati.result.BlackjackHandResult;
import discord.util.dcf.util.MessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public interface BlackjackMessages extends MessageBuilder {

    BlackjackGame getGame();

    User getUser();


    default String getAvatarUrl() {
        return getUser().getAvatarUrl();
    }

    default String getAsTag() {
        return getUser().getAsTag();
    }

    default MessageCreateData playerBlackJack(EmbedBuilder eb) {
        Hand dealerHand = getGame().getDealerHand();
        eb.addField("Dealer's Hand (" + (dealerHand.isSoft() ? "Soft " : "") + dealerHand.getScore() + ")", dealerHand.toString(),
            false);
        Hand playerHand = getGame().getSelectedHand();
        eb.addField("Your Hand (" + (playerHand.isSoft() ? "Soft " : "") + playerHand.getScore() + ")", playerHand.toString(), false);
        eb.setDescription("You have a blackjack! You win " + getFinalWinningsMessage());
        eb.setFooter(getAsTag() + " won!", getAvatarUrl());
        return buildCreate(eb.build());

    }

    default MessageCreateData dealerBlackjack(EmbedBuilder eb) {
        Hand playerHand = getGame().getSelectedHand();
        Hand dealerHand = getGame().getDealerHand();
        eb.addField("Dealer's Hand (" + (dealerHand.isSoft() ? "Soft " : "") + dealerHand.getScore() + ")", dealerHand.toString(),
            false);
        eb.addField("Your Hand (" + (playerHand.isSoft() ? "Soft " : "") + playerHand.getScore() + ")", playerHand.toString(), false);
        eb.setDescription("Dealer has a blackjack!\nYou lose " + getFinalWinningsMessage());
        eb.setFooter(getAsTag() + " lost!", getAvatarUrl());

        return buildCreate(eb.build());
    }

    private String getFinalWinningsMessage() {
        return Emeralds.longMessage(getGame().results().getAbsFinalWinnings());
    }

    private int getAbsFinalWinnings() {
        return getGame().results().getAbsFinalWinnings();
    }

    /**
     * End the game if the player has multiple hands. Dealer hits until 17 or bust, and credits are awarded
     */
    default MessageCreateData endMultiHandGame(EmbedBuilder eb) {
        // Dealer hits until they get 17+
        Hand dealerHand = getGame().getDealerHand();
        while (dealerHand.getScore() < 17) {
            dealerHand.add(getGame().getDeck().deal());
        }

        // Add final score
        eb.addField("Dealer's Hand (" + (dealerHand.isSoft() ? "Soft " : "") + dealerHand.getScore() + ")", dealerHand.toString(),
            false);

        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Hand hand : getGame().getPlayerHands()) {
            int score = hand.getScore();
            sb.append("Hand ");
            sb.append(i++);
            sb.append(": ");
            sb.append(score);
            sb.append(" **");

            int dealerScore = dealerHand.getScore();
            if (score > 21) {
                sb.append("BUST");
                getGame().result(BlackjackHandResult.BUST);
            } else if (score > dealerScore || dealerScore > 21) {
                sb.append("WIN");
                getGame().result(BlackjackHandResult.WIN);
            } else if (score == dealerScore) {
                sb.append("PUSH");
                getGame().result(BlackjackHandResult.PUSH);
            } else {
                sb.append("LOSE");
                getGame().result(BlackjackHandResult.LOSE);
            }

            sb.append("**\n");
        }
        eb.addField("Your Hands", sb.toString(), false);
        int winnings = getAbsFinalWinnings();
        if (winnings > 0) {
            eb.setDescription("You win " + getFinalWinningsMessage());
            eb.setFooter(getUser().getAsTag() + " won!", getUser().getAvatarUrl());
        } else if (winnings < 0) {
            eb.setDescription("You lose " + getFinalWinningsMessage());
            eb.setFooter(getUser().getAsTag() + " lost!", getUser().getAvatarUrl());
        } else {
            eb.setDescription("You don't win or lose any credits**");
            eb.setFooter(getUser().getAsTag() + " tied!", getUser().getAvatarUrl());
        }
        getGame().end();
        return buildCreate(eb.build());
    }

    /**
     * End the game if the player only has 1 hand. Dealer hits until 17 or bust, and credits are awarded.
     */
    default MessageCreateData endOneHandGame(EmbedBuilder eb) {
        Hand hand = getGame().getPlayerHands().get(0);

        // Player busts
        Hand dealerHand = getGame().getDealerHand();
        if (hand.getScore() > 21) {
            getGame().result(BlackjackHandResult.LOSE);
            getGame().end();
            eb.setDescription(
                "You busted!\n You lose " + getFinalWinningsMessage());

            eb.addField("Dealer's Hand (" + (dealerHand.isSoft() ? "Soft " : "") + dealerHand.getScore() + ")", dealerHand.toString(),
                false);
            eb.addField("Your Hand (" + (hand.isSoft() ? "Soft " : "") + hand.getScore() + ")", hand.toString(), false);

            eb.setFooter(getUser().getAsTag() + " lost!", getUser().getAvatarUrl());
            return buildCreate(eb.build());
        }

        // Dealer hits until they get 17+
        while (dealerHand.getScore() < 17) {
            dealerHand.add(getGame().getDeck().deal());
        }

        // Add final score
        eb.addField("Dealer's Hand (" + (dealerHand.isSoft() ? "Soft " : "") + dealerHand.getScore() + ")", dealerHand.toString(),
            false);
        eb.addField("Your Hand (" + (hand.isSoft() ? "Soft " : "") + hand.getScore() + ")", hand.toString(), false);

        // Dealer busts
        if (dealerHand.getScore() > 21) {
            getGame().result(BlackjackHandResult.WIN);
            eb.setDescription(
                "Dealer busted!\n You win " + getFinalWinningsMessage());
            eb.setFooter(getUser().getAsTag() + " won!", getUser().getAvatarUrl());
        } else if (dealerHand.getScore() > hand.getScore()) {
            // Dealer wins
            getGame().result(BlackjackHandResult.LOSE);
            eb.setDescription(
                "The dealer beat you!\n You lose " + getFinalWinningsMessage());
            eb.setFooter(getUser().getAsTag() + " lost!", getUser().getAvatarUrl());
        } else if (hand.getScore() > dealerHand.getScore()) {
            // Player wins
            getGame().result(BlackjackHandResult.WIN);
            eb.setDescription(
                "You beat the dealer!\n You win " + getFinalWinningsMessage());
            eb.setFooter(getUser().getAsTag() + " won!", getUser().getAvatarUrl());
        } else {
            // Tie
            getGame().result(BlackjackHandResult.PUSH);
            eb.setDescription("**You and the dealer tied!\n You don't win or lose any credits**");
            eb.setFooter(getUser().getAsTag() + " tied!", getUser().getAvatarUrl());
        }

        getGame().end();
        return buildCreate(eb.build());
    }

}
