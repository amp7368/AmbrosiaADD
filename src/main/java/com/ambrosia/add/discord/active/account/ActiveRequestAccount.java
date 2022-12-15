package com.ambrosia.add.discord.active.account;

import com.ambrosia.add.database.client.ClientDiscordDetails;
import com.ambrosia.add.database.client.ClientMinecraftDetails;
import com.ambrosia.add.discord.active.base.ActiveRequest;
import com.ambrosia.add.discord.active.ActiveRequestType;
import discord.util.dcf.gui.stored.DCFStoredGui;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.Nullable;

public class ActiveRequestAccount extends ActiveRequest {

    public ClientDiscordDetails discordDetails;
    public ClientMinecraftDetails minecraftDetails;
    private String displayName;

    public ActiveRequestAccount(String displayName, ClientDiscordDetails discordDetails, ClientMinecraftDetails minecraftDetails) {
        super(ActiveRequestType.ACCOUNT.getTypeId());
        this.displayName = displayName;
        this.discordDetails = discordDetails;
        this.minecraftDetails = minecraftDetails;
    }
    public ActiveRequestAccount(){
        super(ActiveRequestType.ACCOUNT.getTypeId());
    }

    @Nullable
    public static ActiveRequestAccount create(Member discord, String minecraft) {
        String displayName = discord.getEffectiveName();
        ClientDiscordDetails discordDetails = new ClientDiscordDetails(discord);
        ClientMinecraftDetails minecraftDetails = ClientMinecraftDetails.fromUsername(minecraft);
        if (minecraftDetails == null) return null;
        return new ActiveRequestAccount(displayName, discordDetails, minecraftDetails);
    }

    @Override
    public DCFStoredGui load(long messageId) {
        return new ActiveRequestAccountGui(messageId,this);
    }
}
