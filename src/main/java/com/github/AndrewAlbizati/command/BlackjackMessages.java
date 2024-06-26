package com.github.AndrewAlbizati.command;

import com.ambrosia.add.discord.util.AmbrosiaColor.AmbrosiaColorGame;
import com.ambrosia.add.discord.util.Emeralds;
import com.github.AndrewAlbizati.game.BlackjackGame;
import com.github.AndrewAlbizati.game.Hand;
import com.github.AndrewAlbizati.result.BlackjackHandResult;
import discord.util.dcf.util.IMessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public interface BlackjackMessages extends IMessageBuilder {

    BlackjackGame getGame();

    User getUser();


    default String getAvatarUrl() {
        return getUser().getAvatarUrl();
    }

    default String getAsTag() {
        return getUser().getName();
    }

    default MessageCreateData playerBlackJack(EmbedBuilder eb) {
        Hand dealerHand = getGame().getDealerHand();
        eb.clearFields();
        eb.addField("Dealer's Hand (" + (dealerHand.isSoft() ? "Soft " : "") + dealerHand.getScore() + ")", dealerHand.toString(),
            false);
        Hand playerHand = getGame().getSelectedHand();
        eb.addField("Your Hand (" + (playerHand.isSoft() ? "Soft " : "") + playerHand.getScore() + ")", playerHand.toString(), false);
        eb.setDescription("You have a blackjack!\n" + getFinalWinningsMessage());
        eb.setFooter(getAsTag() + " won!", getAvatarUrl());
        eb.setColor(AmbrosiaColorGame.EXTRA_WIN);
        return buildCreate(eb.build());
    }

    default MessageCreateData dealerBlackjack(EmbedBuilder eb) {
        Hand playerHand = getGame().getSelectedHand();
        Hand dealerHand = getGame().getDealerHand();
        eb.clearFields();
        eb.addField("Dealer's Hand (" + (dealerHand.isSoft() ? "Soft " : "") + dealerHand.getScore() + ")", dealerHand.toString(),
            false);
        eb.addField("Your Hand (" + (playerHand.isSoft() ? "Soft " : "") + playerHand.getScore() + ")", playerHand.toString(), false);
        eb.setDescription("Dealer has a blackjack!\n" + getFinalWinningsMessage());
        eb.setFooter(getAsTag() + " lost!", getAvatarUrl());
        eb.setColor(AmbrosiaColorGame.LOSE);
        return buildCreate(eb.build());
    }

    private String getFinalWinningsMessage() {
        int winnings = getWinnings();
        String nowHave = Emeralds.longMessage(getGame().getPlayerTotalCredits() + winnings);
        if (getGame().results().getOriginalBet() == 0) return String.format("You bet nothing, so you still have %s", nowHave);
        else if (winnings == 0) return String.format("You tied!\n You still have %s", nowHave);
        String winLose = winnings < 0 ? "lose" : "win";
        String winningsEmeralds = Emeralds.longMessage(Math.abs(winnings));
        return String.format("You %s %s!\nYou now have %s", winLose, winningsEmeralds, nowHave);
    }

    private int getWinnings() {
        return getGame().results().getWinnings();
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
        int handIndex = 1;
        int fakeBet = 4096;
        int fakeWinnings = 0;
        for (Hand hand : getGame().getPlayerHands()) {
            int score = hand.getScore();
            sb.append("Hand ");
            sb.append(handIndex++);
            sb.append(": ");
            sb.append(score);
            sb.append(" **");

            int dealerScore = dealerHand.getScore();
            if (score > 21) {
                sb.append("BUST");
                fakeWinnings += fakeBet * BlackjackHandResult.BUST.betMultiplier();
                getGame().result(BlackjackHandResult.BUST);
            } else if (score > dealerScore || dealerScore > 21) {
                sb.append("WIN");
                fakeWinnings += fakeBet * BlackjackHandResult.WIN.betMultiplier();
                getGame().result(BlackjackHandResult.WIN);
            } else if (score == dealerScore) {
                sb.append("PUSH");
                fakeWinnings += fakeBet * BlackjackHandResult.PUSH.betMultiplier();
                getGame().result(BlackjackHandResult.PUSH);
            } else {
                sb.append("LOSE");
                fakeWinnings += fakeBet * BlackjackHandResult.LOSE.betMultiplier();
                getGame().result(BlackjackHandResult.LOSE);
            }

            sb.append("**\n");
        }
        eb.addField("Your Hands", sb.toString(), false);
        getGame().end();
        eb.setDescription("Split hands!\n" + getFinalWinningsMessage());
        int winnings = getWinnings();
        int originalBet = getGame().results().getOriginalBet();
        if (originalBet == 0) {
            originalBet = fakeBet;
            winnings = fakeWinnings;
        }
        if (winnings > 0) {
            boolean extraWin = winnings / originalBet > 1;
            if (extraWin) eb.setColor(AmbrosiaColorGame.EXTRA_WIN);
            else eb.setColor(AmbrosiaColorGame.WIN);
            eb.setFooter(getUser().getName() + " won!", getUser().getAvatarUrl());
        } else if (winnings < 0) {
            eb.setColor(AmbrosiaColorGame.LOSE);
            eb.setFooter(getUser().getName() + " lost!", getUser().getAvatarUrl());
        } else {
            eb.setColor(AmbrosiaColorGame.TIE);
            eb.setFooter(getUser().getName() + " tied!", getUser().getAvatarUrl());
        }
        return buildCreate(eb.build());
    }

    /**
     * End the game if the player only has 1 hand. Dealer hits until 17 or bust, and credits are awarded.
     */
    default MessageCreateData endOneHandGame(EmbedBuilder eb) {
        Hand hand = getGame().getPlayerHands().get(0);

        Hand dealerHand = getGame().getDealerHand();
        if (hand.getScore() > 21) {
            // Player busts
            getGame().result(BlackjackHandResult.BUST);
            getGame().end();
            eb.setColor(AmbrosiaColorGame.LOSE);
            eb.setDescription("You busted!\n" + getFinalWinningsMessage());

            eb.addField("Dealer's Hand (" + (dealerHand.isSoft() ? "Soft " : "") + dealerHand.getScore() + ")", dealerHand.toString(),
                false);
            eb.addField("Your Hand (" + (hand.isSoft() ? "Soft " : "") + hand.getScore() + ")", hand.toString(), false);
            eb.setFooter(getUser().getName() + " lost!", getUser().getAvatarUrl());
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
            getGame().end();
            eb.setColor(AmbrosiaColorGame.WIN);
            eb.setDescription("Dealer busted!\n" + getFinalWinningsMessage());
            eb.setFooter(getUser().getName() + " won!", getUser().getAvatarUrl());
        } else if (dealerHand.getScore() > hand.getScore()) {
            // Dealer wins
            getGame().result(BlackjackHandResult.LOSE);
            getGame().end();
            eb.setColor(AmbrosiaColorGame.LOSE);
            eb.setDescription("The dealer beat you!\n" + getFinalWinningsMessage());
            eb.setFooter(getUser().getName() + " lost!", getUser().getAvatarUrl());
        } else if (hand.getScore() > dealerHand.getScore()) {
            // Player wins
            getGame().result(BlackjackHandResult.WIN);
            getGame().end();
            eb.setColor(AmbrosiaColorGame.WIN);
            eb.setDescription("You beat the dealer!\n" + getFinalWinningsMessage());
            eb.setFooter(getUser().getName() + " won!", getUser().getAvatarUrl());
        } else {
            // Tie
            getGame().result(BlackjackHandResult.PUSH);
            getGame().end();
            eb.setColor(AmbrosiaColorGame.TIE);
            eb.setDescription("Push!\n" + getFinalWinningsMessage());
            eb.setFooter(getUser().getName() + " tied!", getUser().getAvatarUrl());
        }

        return buildCreate(eb.build());
    }

    void end();
}
