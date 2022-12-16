package com.ambrosia.add.discord.active;

import apple.lib.modules.FileIOService;
import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDInst;
import apple.utilities.gson.adapter.GsonEnumTypeAdapter;
import apple.utilities.json.gson.GsonBuilderDynamic;
import com.ambrosia.add.discord.DiscordBot;
import com.ambrosia.add.discord.DiscordConfig;
import com.ambrosia.add.discord.DiscordModule;
import com.ambrosia.add.discord.active.base.ActiveRequest;
import com.google.gson.Gson;
import discord.util.dcf.gui.stored.DCFStoredGuiFactory;
import java.io.File;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class ActiveRequestDatabase {

    private static AppleAJDInst<ActiveRequestDatabase> manager;
    private static TextChannel requestChannel;

    private final DCFStoredGuiFactory<ActiveRequest<?>> requests = new DCFStoredGuiFactory<>(DiscordBot.dcf);


    public static void load() {
        requestChannel = DiscordBot.dcf.jda().getTextChannelById(DiscordConfig.get().requestChannel);
        File file = DiscordModule.get().getFile("Data", "ActiveRequests.json");
        Gson gson = GsonEnumTypeAdapter.register(ActiveRequestType.values(), new GsonBuilderDynamic(), ActiveRequest.class).create();
        manager = AppleAJD.createInst(ActiveRequestDatabase.class, file, FileIOService.taskCreator(), gson);
        manager.loadOrMake();
    }

    public static void save(ActiveRequest<?> request) {
        manager.getInstance().requests.add(request);
        manager.save();
    }

    public static void remove(ActiveRequest<?> request) {
        manager.getInstance().requests.remove(request);
        manager.save();
    }

    public static MessageCreateAction sendRequest(MessageCreateData message) {
        return requestChannel.sendMessage(message);
    }
}
