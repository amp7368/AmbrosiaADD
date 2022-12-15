package com.ambrosia.add.discord.active;

import apple.lib.modules.FileIOService;
import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDInst;
import com.ambrosia.add.discord.DiscordBot;
import com.ambrosia.add.discord.DiscordConfig;
import com.ambrosia.add.discord.DiscordModule;
import com.ambrosia.add.discord.active.base.ActiveRequest;
import discord.util.dcf.gui.stored.DCFStoredGuiFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class ActiveRequestDatabase {

    private static AppleAJDInst<ActiveRequestDatabase> manager;
    private static TextChannel requestChannel;
    private List<ActiveRequest> requests = new ArrayList<>();

    private static DCFStoredGuiFactory guiFactory;


    public static void load() {
        requestChannel = DiscordBot.dcf.jda().getTextChannelById(DiscordConfig.get().requestChannel);
        guiFactory = new DCFStoredGuiFactory(DiscordBot.dcf);
        File file = DiscordModule.get().getFile("Data", "ActiveRequests.json");
        manager = AppleAJD.createInst(ActiveRequestDatabase.class, file, FileIOService.taskCreator());
        manager.loadOrMake();
        guiFactory.addAll(manager.getInstance().requests);
    }

    public static void save(ActiveRequest request) {
        manager.getInstance().requests.add(request);
        manager.save();
    }

    public static void remove(ActiveRequest request) {
        manager.getInstance().requests.remove(request);
        manager.save();
    }
}
