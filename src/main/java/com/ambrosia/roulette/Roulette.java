package com.ambrosia.roulette;

import apple.lib.modules.AppleModule;
import com.ambrosia.add.discord.DiscordBot;
import com.ambrosia.roulette.game.RouletteCommand;
import com.ambrosia.roulette.gif.CommandRouletteGif;
import com.ambrosia.roulette.table.RouletteTable;
import discord.util.dcf.DCFCommandManager;

public class Roulette extends AppleModule {

    public static final RouletteTable TABLE = new RouletteTable();
    public static final String GAME_NAME = "ROULETTE";

    @Override
    public void onEnable() {
        DCFCommandManager commands = DiscordBot.dcf.commands();
        commands.addCommand(new CommandRouletteGif());
        commands.addCommand(new RouletteCommand());
    }

    @Override
    public String getName() {
        return "Roulette";
    }
}
