package com.ambrosia.roulette;

import apple.lib.modules.AppleModule;
import com.ambrosia.add.discord.DiscordBot;
import com.ambrosia.roulette.bet.RouletteBetCommand;
import com.ambrosia.roulette.gif.CommandRouletteGif;
import com.ambrosia.roulette.table.RouletteTable;

public class Roulette extends AppleModule {

    public static final RouletteTable TABLE = new RouletteTable();

    @Override
    public void onEnable() {
        DiscordBot.dcf.commands().addCommand(new CommandRouletteGif());
        DiscordBot.dcf.commands().addCommand(new RouletteBetCommand());
    }

    @Override
    public String getName() {
        return "Roulette";
    }
}
