package com.ambrosia.add.discord.util;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.discord.DiscordPermissions;
import java.util.function.Function;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.Nullable;

public interface CommandBuilder extends SendMessage {

    String PROFILE_NAME_OPTION = "profile_name";
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
    default ClientEntity findClient(SlashCommandInteractionEvent event) {
        @Nullable String clientName = findOptionProfileName(event);
        if (clientName == null) return null;
        ClientEntity client = ClientStorage.get().find(clientName);
        if (client != null) return client;
        event.replyEmbeds(this.error(String.format("User: '%s' is not in the database", clientName))).queue();
        return null;
    }

    default <T> T findOption(SlashCommandInteractionEvent event, String optionName, Function<OptionMapping, T> getAs) {
        OptionMapping option = event.getOption(optionName);
        if (option == null || getAs.apply(option) == null) {
            this.missingOption(event, optionName);
            return null;
        }
        return getAs.apply(option);
    }

    @Nullable
    default String findOptionProfileName(SlashCommandInteractionEvent event) {
        return findOption(event, PROFILE_NAME_OPTION, OptionMapping::getAsString);
    }

    default void addOptionProfileName(SlashCommandData command) {
        command.addOption(OptionType.STRING, PROFILE_NAME_OPTION, "The name of the profile to be created", true);
    }

    default Integer findOptionAmount(SlashCommandInteractionEvent event) {
        Integer amount = findOption(event, AMOUNT_OPTION, OptionMapping::getAsInt);
        if (amount == null) return null;
        if (amount >= 0) return amount;
        event.replyEmbeds(error(String.format("%s must be positive!", AMOUNT_OPTION))).queue();
        return null;
    }

    default void addOptionAmount(SlashCommandData command) {
        command.addOption(OptionType.STRING, AMOUNT_OPTION, "The amount to change", true);
    }
}
