package com.ambrosia.roulette;

import apple.lib.modules.AppleModule;
import com.ambrosia.add.discord.DiscordBot;
import com.ambrosia.roulette.game.RouletteCommand;
import com.ambrosia.roulette.table.RouletteTable;

public class Roulette extends AppleModule {

    public static final RouletteTable TABLE = new RouletteTable();
    public static final String GAME_NAME = "ROULETTE";

    @Override
    public void onEnable() {
        DiscordBot.dcf.commands().addCommand(new RouletteCommand());
    }

    @Override
    public String getName() {
        return "Roulette";
    }
}
