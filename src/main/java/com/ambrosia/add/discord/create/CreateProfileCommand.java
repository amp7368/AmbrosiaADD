package com.ambrosia.add.discord.create;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.discord.DiscordPermissions;
import lib.DCFSlashCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CreateProfileCommand extends DCFSlashCommand implements CreateProfileMessage {

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        Member sender = event.getMember();
        if (sender == null) return; // this is a guild only command

        boolean hasPermission = DiscordPermissions.get().isDealer(sender.getRoles());
        if (!hasPermission) {
            event.replyEmbeds(this.isNotDealer(event)).queue();
            return;
        }
        this.asyncRun(event);
    }

    private void asyncRun(SlashCommandInteractionEvent event) {
        OptionMapping clientNameOption = event.getOption("name");
        if (clientNameOption == null) {
            event.replyEmbeds(this.error("Please provide a valid 'name'")).queue();
            return;
        }
        String clientName = clientNameOption.getAsString();
        boolean previousExists = ClientStorage.get().find(clientName).isPresent();
        if (previousExists) {
            event.replyEmbeds(this.error(String.format("'%s' is already a user", clientName))).queue();
            return;
        }
        ClientEntity client = new ClientEntity(clientName, event.getUser().getIdLong());
        ClientStorage.get().save(client);
        event.replyEmbeds(this.success(String.format("Successfully created %s", client.displayName))).queue();
    }

    public SlashCommandData getData() {
        return Commands.slash("create", "Create a profile for a customer")
            .addOption(OptionType.STRING, "name", "The name of the customer", true)
            .setDefaultPermissions(DefaultMemberPermissions.DISABLED).setGuildOnly(true);
    }
}
