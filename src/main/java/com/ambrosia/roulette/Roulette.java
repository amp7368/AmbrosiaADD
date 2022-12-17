package com.ambrosia.roulette;

import apple.lib.modules.AppleModule;
import com.ambrosia.add.discord.DiscordBot;

public class Roulette extends AppleModule {

    @Override
    public void onEnable() {
        DiscordBot.dcf.commands().addCommand(new CommandRoulette());
    }

    @Override
    public String getName() {
        return "Roulette";
    }
}
