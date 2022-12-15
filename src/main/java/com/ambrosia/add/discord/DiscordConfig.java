package com.ambrosia.add.discord;

public class DiscordConfig {

    private static DiscordConfig instance;
    public String token = "token";
    public long logChannel = 0;
    public long requestChannel = 0;

    public DiscordConfig() {
        instance = this;
    }

    public static DiscordConfig get() {
        return instance;
    }
}
