package com.ambrosia.add.discord;

import apple.utilities.util.Pretty;
import com.ambrosia.add.database.client.ClientStorage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
        Predicate<String> matchThis = Pattern.compile(matchThisString.isBlank() ? ".*" : matchThisString, Pattern.CASE_INSENSITIVE)
            .asPredicate();

        Stream<Choice> displayNames = ClientStorage.get().allNames().filter(c -> matchThis.test(c.displayName))
            .map(c -> createChoice("", c.displayName, c.displayName));
        Stream<Choice> mcNames = ClientStorage.get().allNames().filter(c -> c.minecraft(mc -> matchThis.test(mc.name), false))
            .map(c -> createChoice("[Minecraft]", c.minecraft.name, c.displayName));
        Stream<Choice> discordNames = ClientStorage.get().allNames()
            .filter(c -> c.discord(dc -> matchThis.test(dc.username) || matchThis.test(dc.guildName), false))
            .map(c -> createChoice("[Discord]", c.discord.guildName, c.displayName));

        Stream<Choice> nonUniqueChoices = Stream.of(displayNames, mcNames, discordNames).flatMap(s -> s);
        List<Choice> choices = new ArrayList<>(
            nonUniqueChoices.collect(Collectors.toMap(Choice::getAsString, choice -> choice, (a, b) -> b)).values());
        choices.sort(Comparator.comparing(Choice::getAsString, String.CASE_INSENSITIVE_ORDER));
        if (choices.size() > OptionData.MAX_CHOICES)
            choices = choices.subList(0, OptionData.MAX_CHOICES);
        event.replyChoices(choices).queue();
    }

    @NotNull
    public Choice createChoice(String title, String name, String value) {
        String choiceName = title.isEmpty() ? name : String.format("%s %s", title, name);
        return new Choice(Pretty.truncate(name, OptionData.MAX_NAME_LENGTH),
            Pretty.truncate(value, OptionData.MAX_CHOICE_VALUE_LENGTH));
    }
}
