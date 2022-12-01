package com.ambrosia.add.discord.profile;

import com.ambrosia.add.database.client.ClientDiscordDetails;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.discord.util.CommandBuilder;
import lib.DCFSlashCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandLink extends DCFSlashCommand implements CommandBuilder {

    public static final String DISCORD_OPTION = "discord";

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        if (!this.isBadPermission(event)) return;
        ClientEntity client = this.findClient(event);
        if (client == null) return;
        Member option = this.findOption(event, DISCORD_OPTION, OptionMapping::getAsMember);
        if (option == null) return;
        client.setDiscord(new ClientDiscordDetails(option));
        client.save();

        event.replyEmbeds(this.embedClientProfile(client)).queue();
    }

    @Override
    public CommandData getData() {
        SlashCommandData command = Commands.slash("link", "Link a client's profile with their discord account");
        addOptionProfileName(command);
        command.addOption(OptionType.USER, DISCORD_OPTION, "The discord account to associate", true);
        return command.setDefaultPermissions(DefaultMemberPermissions.DISABLED).setGuildOnly(true);
    }
}
