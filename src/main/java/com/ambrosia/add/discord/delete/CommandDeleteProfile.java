package com.ambrosia.add.discord.delete;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.discord.util.CommandBuilder;
import discord.util.dcf.slash.DCFSlashSubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandDeleteProfile extends DCFSlashSubCommand implements CommandBuilder {

    @Override
    public SubcommandData getData() {
        SubcommandData command = new SubcommandData("profile", "Delete a profile with 0 transactions");
        addOptionProfileName(command);
        return command;
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        ClientEntity client = this.findClient(event);
        if (client == null) return;
        if (client.hasAnyTransactions()) {
            String msg = String.format("Cannot delete %s's profile. There are entries associated with their account",
                client.displayName);
            event.replyEmbeds(error(msg)).queue();
            return;
        }
        client.delete();
        event.replyEmbeds(success(String.format("Removed profile '%s'", client.displayName))).queue();
    }
}
