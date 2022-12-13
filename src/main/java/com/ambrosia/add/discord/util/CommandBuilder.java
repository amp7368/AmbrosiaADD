package com.ambrosia.add.discord.util;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import java.util.function.Function;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.Nullable;

public interface CommandBuilder extends SendMessage {

    String PROFILE_NAME_OPTION = "profile_name";
    String EMERALD_OPTION = "emeralds";
    String EMERALD_BLOCK_OPTION = "blocks";
    String LIQUID_EMERALD_OPTION = "liquids";

    @Nullable
    default ClientEntity findClient(SlashCommandInteractionEvent event) {
        @Nullable String clientName = findOptionProfileName(event);
        if (clientName == null) return null;
        ClientEntity client = ClientStorage.get().findByName(clientName);
        if (client != null) return client;
        event.replyEmbeds(this.error(String.format("User: '%s' is not in the database", clientName))).queue();
        return null;
    }

    @Nullable
    default <T> T findOption(SlashCommandInteractionEvent event, String optionName, Function<OptionMapping, T> getAs) {
        return findOption(event, optionName, getAs, true);
    }

    default <T> T findOption(SlashCommandInteractionEvent event, String optionName, Function<OptionMapping, T> getAs,
        boolean isRequired) {
        OptionMapping option = event.getOption(optionName);
        if (option == null || getAs.apply(option) == null) {
            if (isRequired)
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

    default void addOptionProfileName(SubcommandData command) {
        command.addOption(OptionType.STRING, PROFILE_NAME_OPTION, "The name of the profile to be created", true);
    }

    default Integer findOptionAmount(SlashCommandInteractionEvent event) {
        Integer e = findOption(event, EMERALD_OPTION, OptionMapping::getAsInt, false);
        Integer eb = findOption(event, EMERALD_BLOCK_OPTION, OptionMapping::getAsInt, false);
        Integer le = findOption(event, LIQUID_EMERALD_OPTION, OptionMapping::getAsInt, false);
        if (e == null) e = 0;
        if (eb == null) eb = 0;
        if (le == null) le = 0;
        int total = e + eb * 64 + le * 64 * 64;
        if (total < 0) {
            event.replyEmbeds(error("Total must be positive!")).setEphemeral(true).queue();
            return null;
        }

        return total;
    }

    default void addOptionAmount(SlashCommandData command) {
        command.addOption(OptionType.INTEGER, LIQUID_EMERALD_OPTION, "The amount in liquid emeralds", false);
        command.addOption(OptionType.INTEGER, EMERALD_BLOCK_OPTION, "The amount in emerald blocks", false);
        command.addOption(OptionType.INTEGER, EMERALD_OPTION, "The amount in emeralds", false);
    }

    default void addOptionAmount(SubcommandData command) {
        command.addOption(OptionType.INTEGER, LIQUID_EMERALD_OPTION, "The amount in liquid emeralds", false);
        command.addOption(OptionType.INTEGER, EMERALD_BLOCK_OPTION, "The amount in emerald blocks", false);
        command.addOption(OptionType.INTEGER, EMERALD_OPTION, "The amount in emeralds", false);
    }

    default void errorRegisterWithStaff(SlashCommandInteractionEvent event) {
        event.reply("Register your discord and/or minecraft with staff").queue();
    }
}
