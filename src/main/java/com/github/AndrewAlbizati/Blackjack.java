package com.github.AndrewAlbizati;

import apple.lib.modules.AppleModule;
import apple.utilities.util.Pretty;
import com.ambrosia.add.discord.DiscordBot;
import com.github.AndrewAlbizati.command.BlackjackCommand;

public class Blackjack extends AppleModule {

    private static Blackjack instance;
    public static final String GAME_NAME = "BLACKJACK";

    public Blackjack() {
        instance = this;
    }

    @Override
    public String getName() {
        return Pretty.upperCaseFirst(GAME_NAME);
    }

    @Override
    public void onEnable() {
        DiscordBot.dcf.commands().addCommand(new BlackjackCommand());
    }

    public static Blackjack get() {
        return instance;
    }
}
