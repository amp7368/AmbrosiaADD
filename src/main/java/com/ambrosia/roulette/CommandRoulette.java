package com.ambrosia.roulette;

import com.ambrosia.add.discord.util.BaseCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandRoulette extends BaseCommand {

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
        String domain = "static.voltskiya.com";
        String url = String.format("https://%s/%d-out.gif", domain, event.getOption("n", OptionMapping::getAsInt));
        EmbedBuilder eb = new EmbedBuilder().setImage(url);
        eb.setTitle("Hellos");
        event.replyEmbeds(eb.build()).queue();
    }

    @Override
    public SlashCommandData getData() {
        SlashCommandData command = Commands.slash("r", "hey");
        return command.addOption(OptionType.INTEGER, "n", "plz", true);
    }

    @Override
    public boolean isOnlyManager() {
        return true;
    }
}
