package com.github.AndrewAlbizati.command;

import com.ambrosia.add.discord.DiscordBot;
import com.ambrosia.add.discord.util.SendMessage;
import discord.util.dcf.slash.DCFSlashSubCommand;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

/**
 * Handles when a user executes the /help command
 */
public class BlackjackHelp extends DCFSlashSubCommand implements SendMessage {

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
        eb.addField("Rules",
            "Dealer must stand on all 17s\n"
                + "Blackjack pays 3 to 2", false);
        eb.setThumbnail(DiscordBot.SELF_USER_AVATAR);
        eb.setColor(new Color(184, 0, 9));
        MessageCreateBuilder message = new MessageCreateBuilder().setEmbeds(eb.build())
            .setComponents(ActionRow.of(Button.link("https://github.com/amp7368/AmbrosiaADD", "More information")));
        event.reply(message.build()).queue();
    }
}
