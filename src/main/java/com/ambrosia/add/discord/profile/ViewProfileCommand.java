package com.ambrosia.add.discord.profile;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.discord.util.CommandBuilder;
import lib.slash.DCFSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class ViewProfileCommand extends DCFSlashCommand implements CommandBuilder {


    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        if (this.isBadPermission(event)) return;
        ClientEntity client = this.findClient(event);
        if (client == null) return;
        event.replyEmbeds(embedClientProfile(client)).queue();
    }


    @Override
    public SlashCommandData getData() {
        SlashCommandData command = Commands.slash("profile", "View a client's profile");
        addOptionProfileName(command);
        return command.setDefaultPermissions(DefaultMemberPermissions.ENABLED).setGuildOnly(true);
    }
}
