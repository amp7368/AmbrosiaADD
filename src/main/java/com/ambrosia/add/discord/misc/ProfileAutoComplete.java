package com.ambrosia.add.discord.misc;

import apple.utilities.util.Pretty;
import com.ambrosia.add.database.client.ClientEntity;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

public class ProfileAutoComplete extends ListenerAdapter {

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if (!event.getFocusedOption().getName().equals("profile_name")) return;
        String matchThisString = event.getFocusedOption().getValue().replace("@", "");
        List<Choice> choices = ClientSearch.autoComplete(matchThisString).stream()
            .map(this::createChoice)
            .toList();
        event.replyChoices(choices).queue();
    }

    @NotNull
    public Choice createChoice(ClientEntity client) {
        String value = client.getDisplayName();
        return new Choice(Pretty.truncate(value, OptionData.MAX_NAME_LENGTH),
            Pretty.truncate(value, OptionData.MAX_CHOICE_VALUE_LENGTH));
    }
}
