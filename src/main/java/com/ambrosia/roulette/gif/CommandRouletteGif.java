package com.ambrosia.roulette.gif;

import com.ambrosia.add.discord.DiscordModule;
import com.ambrosia.add.discord.util.BaseCommand;
import com.ambrosia.roulette.img.RouletteImage;
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
        DCFGui gui = new DCFGui(dcf, (m) -> event.reply(m).setEphemeral(ephemeral));
        Integer num = event.getOption("n", OptionMapping::getAsInt);
        if (num == null) return;
        String url = RouletteImage.spin(num).gif();
        DiscordModule.get().logger().warn(url);
        gui.addPage(new RouletteGifGui(gui, url));
        gui.send();
    }

    @Override
    public SlashCommandData getData() {
        SlashCommandData command = Commands.slash("r", "hey");
        command.addOption(OptionType.INTEGER, "n", "What number to roll", true);
        return command;
    }

    @Override
    public boolean isOnlyManager() {
        return true;
    }
}
