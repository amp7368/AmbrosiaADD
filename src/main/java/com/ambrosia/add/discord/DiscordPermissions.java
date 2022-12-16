package com.ambrosia.add.discord;

import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

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
    private final List<Long> allowedServers = List.of();

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

    public boolean wrongServer(Guild guild) {
        return guild == null || !allowedServers.contains(guild.getIdLong());
    }

    public void generateWarnings() {
        if (this.dealerRole.isEmpty()) DiscordModule.get().logger().warn(warn("dealerRole"));
        if (this.managerRole.isEmpty()) DiscordModule.get().logger().warn(warn("managerRole"));
        if (this.allowedServers.isEmpty()) DiscordModule.get().logger().warn(warn("allowedServers"));
    }

    @NotNull
    public String warn(String missingField) {
        return missingField + " is not set in /Permissions.config.json";
    }
}
