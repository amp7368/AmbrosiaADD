package com.ambrosia.add;

import apple.lib.modules.AppleModule;
import apple.lib.modules.ApplePlugin;
import com.ambrosia.add.discord.DiscordModule;
import java.util.Collection;
import java.util.List;

public class Ambrosia extends ApplePlugin {

    public static void main(String[] args) {
        new Ambrosia().run();
    }

    @Override
    public Collection<AppleModule> getModules() {
        return List.of(new DiscordModule());
    }

    @Override
    public String getName() {
        return "Ambrosia Add";
    }
}
