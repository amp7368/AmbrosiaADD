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

    private final List<Long> dealerRole = List.of();
    private final List<Long> managerRole = List.of();

    public boolean isDealer(List<Role> roles) {
        for (Role role : roles) {
            if (dealerRole.contains(role.getIdLong())) return true;
        }
        return false;
    }
    public boolean isManager(List<Role> roles) {
        for (Role role : roles) {
            if (managerRole.contains(role.getIdLong())) return true;
        }
        return false;
    }
}
