package com.github.AndrewAlbizati;

import apple.lib.modules.AppleModule;
import apple.utilities.util.Pretty;
import com.ambrosia.add.discord.DiscordBot;
import com.github.AndrewAlbizati.command.BlackjackCommand;
import com.github.AndrewAlbizati.command.HelpCommand;
import com.github.AndrewAlbizati.game.Deck;

public class Blackjack extends AppleModule {

    private final Deck deck;
    private static Blackjack instance;
    public static final String GAME_NAME = "BLACKJACK";

    public Blackjack() {
        instance = this;
        deck = new Deck(6);
        deck.shuffleDeck();
    }

    @Override
    public String getName() {
        return Pretty.upperCaseFirst(GAME_NAME);
    }

    public Deck getDeck() {
        return deck;
    }

    @Override
    public void onEnable() {
        DiscordBot.dcf.commands().addCommand(new BlackjackCommand(), new HelpCommand());
    }

    public static Blackjack get() {
        return instance;
    }
}
