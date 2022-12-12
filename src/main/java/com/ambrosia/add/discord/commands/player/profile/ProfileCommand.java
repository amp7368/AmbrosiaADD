package com.ambrosia.add.discord.commands.player.profile;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.discord.util.BaseCommand;
import com.ambrosia.add.discord.util.CommandBuilder;
import discord.util.dcf.slash.DCFSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class ProfileCommand extends BaseCommand {

    @Override
    public void onCheckedCommand(SlashCommandInteractionEvent event) {
        ClientEntity client = ClientStorage.get().findByDiscord(event.getUser().getIdLong());
        if (client == null) {
            this.errorRegisterWithStaff(event);
            return;
        }
        event.replyEmbeds(embedClientProfile(client)).queue();
    }


    @Override
    public SlashCommandData getData() {
        SlashCommandData command = Commands.slash("profile", "View your profile");
        return command.setGuildOnly(true);
    }

}
