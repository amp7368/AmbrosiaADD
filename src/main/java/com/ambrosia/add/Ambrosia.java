package com.ambrosia.add;

import apple.lib.modules.AppleModule;
import apple.lib.modules.ApplePlugin;
import com.ambrosia.add.api.AmbrosiaAPI;
import com.ambrosia.add.database.AmbrosiaDatabaseModule;
import com.ambrosia.add.discord.DiscordModule;
import java.security.SecureRandom;
import java.util.List;
import java.util.StringJoiner;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class Ambrosia extends ApplePlugin {

    public static final String DISCORD_INVITE_LINK = "https://discord.gg/tEAy2dGXWF";
    private static Ambrosia instance;

    public Ambrosia() {
        instance = this;
    }

    public static void main(String[] args) {
        checkRandom();
        new Ambrosia().start();
    }

    private static void checkRandom() {
        SecureRandom random = new SecureRandom();
        double trials = 1000000;
        int[] bins = new int[10];
        for (int i = 0; i < trials; i++) {
            double rand = random.nextDouble();
            for (int bin = 1; bin <= bins.length; bin++) {
                if (rand <= bin / (double) bins.length) {
                    bins[bin - 1]++;
                    break;
                }
            }
        }
        StringJoiner joiner = new StringJoiner("\n");
        for (int i = 0; i < bins.length; i++) {
            int bin = bins[i];
            double lower = i / (double) bins.length;
            double upper = (i + 1) / (double) bins.length;
            String header = "%.2f <= %.2f: ".formatted(lower, upper);
            String value = "%.4f%%".formatted(bin / trials);
            joiner.add(header + value);
        }
        System.out.println("Randomness Check:\n" + joiner);
    }

    public static Ambrosia get() {
        return instance;
    }

    @NotNull
    public static Button inviteButton() {
        return Button.link(Ambrosia.DISCORD_INVITE_LINK, "Ambrosia Discord Server");
    }

    @Override
    public List<AppleModule> createModules() {
        return List.of(new AmbrosiaDatabaseModule(), new AmbrosiaAPI(), new DiscordModule());
    }

    @Override
    public String getName() {
        return "ADD";
    }
}
