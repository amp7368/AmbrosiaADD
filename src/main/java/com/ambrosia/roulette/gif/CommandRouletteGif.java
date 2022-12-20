package com.ambrosia.roulette.gif;

import com.ambrosia.add.discord.DiscordModule;
import com.ambrosia.add.discord.util.BaseCommand;
import discord.util.dcf.gui.base.gui.DCFGui;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandRouletteGif extends BaseCommand {

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
        Boolean ephemeral = event.getOption("ephemeral", false, OptionMapping::getAsBoolean);
        String ext = event.getOption("ext", "gif", OptionMapping::getAsString);
        DCFGui gui = new DCFGui(dcf, (m) -> event.reply(m).setEphemeral(ephemeral));
        String domain = "static.voltskiya.com";
        Integer iteration = event.getOption("iteration", 101, OptionMapping::getAsInt);
        Integer num = event.getOption("n",  OptionMapping::getAsInt);
        Integer version = event.getOption("version",  OptionMapping::getAsInt);
        String url = String.format("https://%s/%d/%d-%02d.%d.%s", domain, iteration, iteration, num,version, ext);
        DiscordModule.get().logger().warn(url);
        gui.addPage(new RouletteGifGui(gui, url));
        gui.send();
    }

    @Override
    public SlashCommandData getData() {
        SlashCommandData command = Commands.slash("r", "hey");
        command.addOption(OptionType.INTEGER, "iteration", "The iteration", true);
        command.addOption(OptionType.INTEGER, "n", "What number to roll", true);
        command.addOption(OptionType.INTEGER, "version", "A random choice", true);
        command.addOption(OptionType.STRING, "ext", "extension");
        command.addOption(OptionType.BOOLEAN, "ephemeral", "coolness");
        return command;
    }

    @Override
    public boolean isOnlyManager() {
        return true;
    }
}
