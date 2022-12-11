package com.ambrosia.add.discord;

import apple.lib.modules.AppleModule;
import apple.lib.modules.configs.factory.AppleConfigLike;
import com.ambrosia.add.discord.commands.CommandHelp;
import com.ambrosia.add.discord.commands.casino.CommandCasino;
import com.ambrosia.add.discord.commands.delete.CommandDelete;
import com.ambrosia.add.discord.commands.operation.CommandCash;
import com.ambrosia.add.discord.commands.profile.CommandLink;
import com.ambrosia.add.discord.commands.profile.CreateProfileCommand;
import com.ambrosia.add.discord.commands.profile.ProfileCommand;
import com.ambrosia.add.discord.commands.profile.ViewProfileCommand;
import com.ambrosia.add.discord.log.DiscordLog;
import com.github.AndrewAlbizati.Blackjack;
import discord.util.dcf.DCF;
import discord.util.dcf.DCFCommandManager;
import java.util.List;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class DiscordModule extends AppleModule {

    public static final String AMBROSIA_ICON =
        "https://cdn.discordapp" + ".com/icons/923749890104885271/a_52da37c184005a14d15538cb62271b9b.webp";
    private static DiscordModule instance;

    public DiscordModule() {
        instance = this;
    }

    public static DiscordModule get() {
        return instance;
    }

    @Override
    public void onEnable() {
        JDABuilder builder = JDABuilder.createDefault(DiscordConfig.get().token);
        JDA jda = builder.build();
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        jda.getPresence().setPresence(Activity.playing("!"), false);

        DCF dcf = new DCF(jda);
        DiscordBot.SELF_USER_AVATAR = jda.getSelfUser().getAvatarUrl();
        DiscordBot.dcf = dcf;

        DCFCommandManager dcfCommands = dcf.commands();
        dcfCommands.addCommand(new CreateProfileCommand(), new ProfileCommand(), new ViewProfileCommand(), new CommandLink());
        dcfCommands.addCommand(new CommandCash());
        dcfCommands.addCommand(new CommandDelete(), new CommandCasino());
        dcfCommands.addCommand(new CommandHelp());
        new DiscordLog(dcf);
    }

    @Override
    public void onEnablePost() {
        DiscordBot.dcf.commands().updateCommands();
    }

    @Override
    public List<AppleModule> createModules() {
        return List.of(new Blackjack());
    }

    @Override
    public List<AppleConfigLike> getConfigs() {
        return List.of(configFolder("Config", configJson(DiscordConfig.class, "Discord.config"),
            configJson(DiscordPermissions.class, "Permissions.config")));
    }

    @Override
    public String getName() {
        return "Discord";
    }
}
