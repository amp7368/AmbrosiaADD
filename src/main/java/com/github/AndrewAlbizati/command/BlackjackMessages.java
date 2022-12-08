package com.github.AndrewAlbizati.command;

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
        eb.setDescription("**You have a blackjack! You win " + (long) Math.ceil(getGame().getBet() * 1.5) + " credits!**");
        eb.setFooter(getAsTag() + " won!", getAvatarUrl());
        return buildCreate(eb.build());

    }

    default MessageCreateData dealerBlackjack(EmbedBuilder eb) {
        Hand playerHand = getGame().getSelectedHand();
        Hand dealerHand = getGame().getDealerHand();
        eb.addField("Dealer's Hand (" + (dealerHand.isSoft() ? "Soft " : "") + dealerHand.getScore() + ")", dealerHand.toString(),
            false);
        eb.addField("Your Hand (" + (playerHand.isSoft() ? "Soft " : "") + playerHand.getScore() + ")", playerHand.toString(), false);
        eb.setDescription(
            "**Dealer has a blackjack! You lose " + getGame().getBet() + " point" + (getGame().getBet() == 1 ? "" : "s") + "**");
        eb.setFooter(getAsTag() + " lost!", getAvatarUrl());

        return buildCreate(eb.build());
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
            eb.setDescription("**You busted! You lose " + getGame().getBet() + " point" + (getGame().getBet() != 1 ? "s" : "") + "**");

            eb.addField("Dealer's Hand (" + (dealerHand.isSoft() ? "Soft " : "") + dealerHand.getScore() + ")", dealerHand.toString(),
                false);
            eb.addField("Your Hand (" + (hand.isSoft() ? "Soft " : "") + hand.getScore() + ")", hand.toString(), false);

            eb.setFooter(getUser().getAsTag() + " lost!", getUser().getAvatarUrl());
            getGame().result(BlackjackHandResult.LOSE);
            getGame().end();
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
            eb.setDescription(
                "**Dealer busted! You win " + getGame().getBet() + " point" + (getGame().getBet() != 1 ? "s" : "") + "**");
            eb.setFooter(getUser().getAsTag() + " won!", getUser().getAvatarUrl());
            getGame().result(BlackjackHandResult.WIN);
        } else if (dealerHand.getScore() > hand.getScore()) {
            // Dealer wins
            eb.setDescription(
                "**The dealer beat you! You lose " + getGame().getBet() + " point" + (getGame().getBet() != 1 ? "s" : "") + "**");
            eb.setFooter(getUser().getAsTag() + " lost!", getUser().getAvatarUrl());
            getGame().result(BlackjackHandResult.LOSE);
        } else if (hand.getScore() > dealerHand.getScore()) {
            // Player wins
            eb.setDescription(
                "**You beat the dealer! You win " + getGame().getBet() + " point" + (getGame().getBet() != 1 ? "s" : "") + "**");
            eb.setFooter(getUser().getAsTag() + " won!", getUser().getAvatarUrl());
            getGame().result(BlackjackHandResult.WIN);
        } else {
            // Tie
            eb.setDescription("**You and the dealer tied! You don't win or lose any credits**");
            eb.setFooter(getUser().getAsTag() + " tied!", getUser().getAvatarUrl());
            getGame().result(BlackjackHandResult.PUSH);
        }

        getGame().end();
        return buildCreate(eb.build());
    }

}
