package com.ambrosia.add.discord;

import java.util.List;
import net.dv8tion.jda.api.entities.Role;

public class DiscordPermissions {

    private static DiscordPermissions instance;

    public DiscordPermissions() {
        instance = this;
    }

    public static DiscordPermissions get() {
        return instance;
    }

    private long dealerRole = 0;
    private long managerRole = 0;

    public boolean isDealer(List<Role> roles) {
        for (Role role : roles) {
            if (role.getIdLong() == dealerRole) return true;
        }
        return false;
    }
}
