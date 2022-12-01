package com.ambrosia.add.discord;

public class DiscordConfig {

    private static DiscordConfig instance;
    public String token;

    public DiscordConfig() {
        instance = this;
    }

    public static DiscordConfig get() {
        return instance;
    }
}
