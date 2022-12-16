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

    public boolean isConfigured() {
        return !this.token.equals("token");
    }

    public void generateWarnings() {
        if (logChannel == 0) DiscordModule.get().logger().warn("Log channel is not set");
        if (requestChannel == 0) DiscordModule.get().logger().warn("Request channel is not set");
    }
}
