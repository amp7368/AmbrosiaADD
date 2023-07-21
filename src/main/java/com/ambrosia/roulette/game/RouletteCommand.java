package com.ambrosia.roulette.game;

import com.ambrosia.add.discord.util.BaseCommand;
import com.ambrosia.roulette.game.bet.RouletteBetSubCommand;
import com.ambrosia.roulette.game.table.RouletteTableSubCommand;
import discord.util.dcf.slash.DCFSlashSubCommand;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class RouletteCommand extends BaseCommand {

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("roulette", "Play a roulette game");
    }

    @Override
    public List<DCFSlashSubCommand> getSubCommands() {
        return List.of(new RouletteBetSubCommand(), new RouletteTableSubCommand());
    }
}
