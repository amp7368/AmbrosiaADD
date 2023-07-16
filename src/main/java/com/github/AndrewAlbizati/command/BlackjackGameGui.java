package com.github.AndrewAlbizati.command;

import com.ambrosia.add.discord.util.AmbrosiaColor.AmbrosiaColorGame;
import com.github.AndrewAlbizati.game.BlackjackGame;
import com.github.AndrewAlbizati.game.Card;
import com.github.AndrewAlbizati.game.Hand;
import com.github.AndrewAlbizati.result.BlackjackHandResult;
import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class BlackjackGameGui extends DCFGuiPage<DCFGui> implements BlackjackMessages {

    private static final Button STAND_BUTTON = Button.danger("stand", "Stand");
    private static final Button HIT_BUTTON = Button.success("hit", "Hit");
    private static final Button DOUBLE_DOWN_BUTTON = Button.primary("dd", "Double Down");
    private static final Button SPLIT_BUTTON = Button.primary("split", "Split");
    private final User user;
    private final BlackjackGame game;

    public BlackjackGameGui(DCFGui dcfGui, User user, BlackjackGame game) {
        super(dcfGui);
        this.user = user;
        this.game = game;
        registerButton(STAND_BUTTON.getId(), this::stand);
        registerButton(SPLIT_BUTTON.getId(), this::split);
        registerButton(HIT_BUTTON.getId(), this::hit);
        registerButton(DOUBLE_DOWN_BUTTON.getId(), this::doubleDown);
    }


    @Override
    public BlackjackGame getGame() {
        return this.game;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    private void doubleDown(ButtonInteractionEvent event) {
        if (isWrongUser(event)) return;
        game.doubleDown();
        this.hit(event);
        getGame().setGameComplete();
    }

    private void hit(ButtonInteractionEvent event) {
        if (isWrongUser(event)) return;
        Hand playerHand = game.getSelectedHand();
        playerHand.add(game.getDeck().deal());

        // End hand if player busts
        if (playerHand.getScore() >= 21) {
            game.setHandComplete();
        }
    }

    private void stand(ButtonInteractionEvent event) {
        if (isWrongUser(event)) return;
        game.setHandComplete();
    }

    private boolean isWrongUser(ButtonInteractionEvent event) {
        return event.getUser().getIdLong() != getUser().getIdLong();
    }

    private void split(ButtonInteractionEvent event) {
        if (isWrongUser(event)) return;
        Hand hand1 = game.getSelectedHand();

        Card card1 = hand1.get(0);
        Card card2 = hand1.get(1);

        hand1.clear();
        hand1.add(card1);

        Hand hand2 = new Hand();
        hand2.add(card2);
        game.getPlayerHands().add(hand2);

        game.getPlayerHands().get(game.getSelectedHandIndex()).add(game.getDeck().deal());
        game.getPlayerHands().get(game.getSelectedHandIndex() + 1).add(game.getDeck().deal());
    }

    @Override
    public void remove() {
        if (!game.isGameComplete()) {
            getGame().setGameComplete();
            // forcefully edit the message because gui has expired
            editMessage(makeEditMessage());
            getGame().end();
        }
    }

    @Override
    public MessageCreateData makeMessage() {
        // Create new embed with all current game information
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Blackjack");
        eb.setDescription(
            "You bet **" + game.getCurrentBet() + "** credit" + (game.getCurrentBet() != 1 ? "s" : "") + "\n" + "You have **"
                + game.getPlayerTotalCredits() + "** credit" + (game.getPlayerTotalCredits() != 1 ? "s" : "") + "\n\n" + "**Rules**\n"
                + "Dealer must stand on all 17s\n" + "Blackjack pays 3 to 2");
        eb.setColor(AmbrosiaColorGame.IN_PROGRESS);
        eb.setFooter("Game with " + user.getAsTag(), user.getAvatarUrl());

        // Show the dealer's up card and the players hand
        eb.addField("Dealer's Hand", game.getDealerHand().get(0).toString(), false);
        eb.addField("Your Hand (" + (game.getSelectedHand().isSoft() ? "Soft " : "") + game.getSelectedHand().getScore() + ")",
            game.getSelectedHand().toString(), false);
        // Display multiple hands if user has split
        if (game.hasSplit()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < game.getPlayerHands().size(); i++) {
                Hand hand = game.getPlayerHands().get(i);
                if (i == game.getSelectedHandIndex()) {
                    sb.append("**");
                }

                sb.append("Hand ");
                sb.append((i + 1));
                sb.append(": ");

                sb.append((hand.isSoft() ? "Soft " : ""));
                sb.append(hand.getScore());
                if (i == game.getSelectedHandIndex()) {
                    sb.append("**");
                }

                sb.append("\n");
            }
            eb.addField("Other Hands", sb.toString(), false);
        }

        Hand playerHand = game.getPlayerHands().get(0);

        // Player is dealt a Blackjack
        boolean playerBlackjack = playerHand.getScore() == 21 && playerHand.size() == 2 && !game.hasSplit();
        boolean dealerBlackjack = game.getDealerHand().getScore() == 21 && game.getDealerHand().size() == 2 && !game.hasSplit();
        if (playerBlackjack && !dealerBlackjack) {
            getGame().result(BlackjackHandResult.BLACKJACK);
            end();
            return this.playerBlackJack(eb);
        }

        // Dealer is dealt a Blackjack
        if (dealerBlackjack && !playerBlackjack) {
            getGame().result(BlackjackHandResult.LOSE);
            getGame().end();
            return dealerBlackjack(eb);
        }
        if (dealerBlackjack) {
            game.setGameComplete();
        }
        // Game is completed
        if (game.isGameComplete()) {
            eb.clearFields();
            if (game.hasSplit()) {
                return this.endMultiHandGame(eb);
            } else return this.endOneHandGame(eb);
        }
        // Determine which buttons should be displayed

        ActionRow components;
        boolean canDoubleBet = game.canReserve(game.results().getCurrentBet() * 2);
        boolean isFirstHand = game.getSelectedHand().size() == 2;
        if (canDoubleBet && !game.hasSplit() && isFirstHand) {
            boolean canSplit = playerHand.get(0).equals(playerHand.get(1));
            if (canSplit) {
                components = ActionRow.of(HIT_BUTTON, STAND_BUTTON, DOUBLE_DOWN_BUTTON, SPLIT_BUTTON);
            } else {
                components = ActionRow.of(HIT_BUTTON, STAND_BUTTON, DOUBLE_DOWN_BUTTON);
            }
        } else {
            components = ActionRow.of(HIT_BUTTON, STAND_BUTTON);
        }
        return buildCreate(eb.build(), components);
    }

    @Override
    public void end() {
        getGame().end();
        this.remove();
    }


}
