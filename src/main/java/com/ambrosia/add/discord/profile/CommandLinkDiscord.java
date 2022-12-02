package com.ambrosia.add.discord.profile;

import com.ambrosia.add.database.client.ClientDiscordDetails;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.discord.log.DiscordLog;
import com.ambrosia.add.discord.util.CommandBuilder;
import discord.util.dcf.slash.DCFSlashSubCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandLinkDiscord extends DCFSlashSubCommand implements CommandBuilder {

    public static final String DISCORD_OPTION = "discord";

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        if (this.isBadPermission(event)) return;
        ClientEntity client = this.findClient(event);
        if (client == null) return;
        Member option = this.findOption(event, DISCORD_OPTION, OptionMapping::getAsMember);
        if (option == null) return;
        client.setDiscord(new ClientDiscordDetails(option));
        if (client.trySave()) {
            event.replyEmbeds(this.embedClientProfile(client)).queue();
            DiscordLog.log().modifyDiscord(client, event.getUser());
        } else event.replyEmbeds(this.error("Discord was already assigned")).queue();
    }

    @Override
    public SubcommandData getData() {
        SubcommandData command = new SubcommandData("discord", "Link a client's profile with their discord account");
        addOptionProfileName(command);
        return command.addOption(OptionType.USER, DISCORD_OPTION, "The discord account to associate", true);
    }
}
