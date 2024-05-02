package com.ambrosia.roulette;

import apple.lib.modules.AppleModule;
import com.ambrosia.add.discord.DiscordBot;
import com.ambrosia.roulette.game.RouletteCommand;
import com.ambrosia.roulette.table.RouletteSpace;
import com.ambrosia.roulette.table.RouletteSpaceTypeAdapter;
import com.ambrosia.roulette.table.RouletteStreet;
import com.ambrosia.roulette.table.RouletteStreetTypeAdapter;
import com.ambrosia.roulette.table.RouletteTable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;

public class Roulette extends AppleModule {

    public static final RouletteTable TABLE = new RouletteTable();
    public static final String GAME_NAME = "ROULETTE";

    public static Gson gson() {
        return new GsonBuilder()
            .registerTypeAdapter(RouletteSpace.class, new RouletteSpaceTypeAdapter())
            .registerTypeAdapter(RouletteStreet.class, new RouletteStreetTypeAdapter())
            .create();
    }

    @Override
    public void onEnable() {
        DiscordBot.dcf.commands().addCommand(new RouletteCommand());
    }

    @Override
    public List<AppleModule> createModules() {
        return List.of();
    }

    @Override
    public String getName() {
        return "Roulette";
    }
}
