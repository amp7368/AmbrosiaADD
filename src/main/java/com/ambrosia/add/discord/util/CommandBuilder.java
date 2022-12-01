package com.ambrosia.add.discord.util;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.discord.DiscordPermissions;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandBuilder extends SendMessage {

    String PROFILE_NAME_OPTION = "name";
    String AMOUNT_OPTION = "amount";

    default boolean isBadPermission(SlashCommandInteractionEvent event) {
        Member sender = event.getMember();
        if (sender == null) {
            event.replyEmbeds(this.error("Guilds only")).queue(); // this is a guild only command
            return false;
        }
        boolean hasPermission = DiscordPermissions.get().isDealer(sender.getRoles());
        if (!hasPermission) {
            event.replyEmbeds(this.isNotDealer(event)).queue();
            return false;
        }
        return true;
    }

    @Nullable
    default ClientEntity findClient(SlashCommandInteractionEvent event, @NotNull String clientName) {
        ClientEntity client = ClientStorage.get().find(clientName);
        if (client != null) return client;
        event.replyEmbeds(this.error(String.format("User: '%s' is not in the database", clientName))).queue();
        return null;
    }

    @Nullable
    default String getOptionProfileName(SlashCommandInteractionEvent event) {
        OptionMapping clientNameOption = event.getOption(PROFILE_NAME_OPTION);
        if (clientNameOption != null) return clientNameOption.getAsString();
        event.replyEmbeds(this.missingOption(PROFILE_NAME_OPTION)).queue();
        return null;
    }

    default void addOptionProfileName(SlashCommandData command) {
        command.addOption(OptionType.STRING, PROFILE_NAME_OPTION, "The name of the profile to be created", true);
    }

    default Integer getOptionAmount(SlashCommandInteractionEvent event) {
        OptionMapping amountOption = event.getOption(AMOUNT_OPTION);
        if (amountOption == null) {
            event.replyEmbeds(this.missingOption(AMOUNT_OPTION)).queue();
            return null;
        }
        if (amountOption.getAsInt() < 0) {
            event.replyEmbeds(error(String.format("%s must be positive!", AMOUNT_OPTION))).queue();
            return null;
        }
        return amountOption.getAsInt();
    }

    default void addOptionAmount(SlashCommandData command) {
        command.addOption(OptionType.STRING, AMOUNT_OPTION, "The amount to change", true);
    }
}
