package com.ambrosia.add;

import apple.lib.modules.AppleModule;
import apple.lib.modules.ApplePlugin;
import com.ambrosia.add.database.AmbrosiaDatabase;
import com.ambrosia.add.discord.DiscordModule;
import java.util.List;

public class Ambrosia extends ApplePlugin {

    public static void main(String[] args) {
        new Ambrosia().start();
    }

    @Override
    public List<AppleModule> createModules() {
        return List.of(new AmbrosiaDatabase(), new DiscordModule());
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String getName() {
        return "ADD";
    }
}
