package com.ambrosia.add.discord.commands.profile;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.discord.util.BaseCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class ViewProfileCommand extends BaseCommand {

    @Override
    public void onCheckedCommand(SlashCommandInteractionEvent event) {
        ClientEntity client = this.findClient(event);
        if (client == null) return;
        event.replyEmbeds(embedClientProfile(client)).queue();
    }

    @Override
    public boolean isOnlyDealer() {
        return true;
    }

    @Override
    public SlashCommandData getData() {
        SlashCommandData command = Commands.slash("profile_view", "View a client's profile");
        addOptionProfileName(command);
        return command.setDefaultPermissions(DefaultMemberPermissions.DISABLED).setGuildOnly(true);
    }
}
