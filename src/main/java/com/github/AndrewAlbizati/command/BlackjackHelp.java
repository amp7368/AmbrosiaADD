package com.github.AndrewAlbizati.command;

import com.ambrosia.add.discord.DiscordBot;
import discord.util.dcf.slash.DCFSlashSubCommand;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

/**
 * Handles when a user executes the /help command
 */
public class BlackjackHelp extends DCFSlashSubCommand {

    @Override
    public SubcommandData getData() {
        return new SubcommandData("blackjack", "Gives instructions on how to play");
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Blackjack Help");
        eb.addField("How to Play",
            "You and the dealer are dealt 2 cards in the beginning of the game. You can see both of your cards and one of the "
                + "dealer's cards. The goal of the game is to get your card total close to 21 without going over. Each card is "
                + "valued normally, except that face cards are worth 10, and aces are worth 1 or 11 (depending on if it goes over "
                + "the total).", false);
        eb.addField("Controls",
            "When a game is started, you can hit (draw a card) or stand (stop drawing cards). If you have enough credits, you can "
                + "also double down (doubles your bet but you are only given 1 more card). If both of the cards are the same number,"
                + " they can also be split (starts 2 Blackjack hands in the same game with each of the cards).", false);
        eb.addField("Strategy",
            "A good strategy for Blackjack is to hit until your total is 17 or above, and then stand. Doubling down on totals of 10 "
                + "and 11 are also generally favorable for the player.", false);
        eb.addField("About",
            "For more information about the original variation of the blackjack bot, click [here](https://github"
                + ".com/AndrewAlbizati/blackjack-discord-bot).",
            false);
        eb.setThumbnail(DiscordBot.SELF_USER_AVATAR);
        eb.setColor(new Color(184, 0, 9));

        event.replyEmbeds(eb.build()).queue();
    }
}
