package com.ambrosia.add.discord.commands.player.help;

import com.ambrosia.add.discord.util.AmbrosiaColor;
import discord.util.dcf.gui.base.gui.DCFGui;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;

public class HelpBlackjackPage extends HelpGuiPage {

    public HelpBlackjackPage(DCFGui dcfGui) {
        super(dcfGui);
    }

    @Override
    protected ActionRow pageActionRow() {
        return ActionRow.of(BLACKJACK_WIKI);
    }

    @Override
    protected String getTitle() {
        return "Blackjack";
    }

    @Override
    public MessageEmbed makeEmbed(EmbedBuilder eb) {
        eb.addField("How to Play",
            "You and the dealer are dealt 2 cards in the beginning of the game. You can see both of your cards and one of the "
                + "dealer's cards. The goal of the game is to get your card total close to 21 without going over. Each card is "
                + "valued normally, except that face cards are worth 10, and aces are worth 1 or 11 (depending on if it goes over "
                + "the total).", false);
        eb.addField("Controls",
            "When a game is started, you can hit (draw a card) or stand (stop drawing cards). If you have enough credits, you can "
                + "also double down (doubles your bet but you are only given 1 more card). If both of the cards are the same number,"
                + " they can also be split (starts 2 Blackjack hands in the same game with each of the cards).", false);
        eb.addField("Rules", """
            Dealer must stand on 17 or above
            A Blackjack pays 3 to 2
            Each hand is played with 2 decks, which are shuffled every hand
            """, false);
        return eb.build();
    }

    @Override
    protected int color() {
        return AmbrosiaColor.BLACKJACK;
    }
}
