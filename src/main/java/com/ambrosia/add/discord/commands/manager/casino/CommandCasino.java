package com.ambrosia.add.discord.commands.manager.casino;

import com.ambrosia.add.discord.commands.manager.casino.game.CommandCasinoGames;
import com.ambrosia.add.discord.commands.manager.casino.ratio.CommandCasinoRatio;
import com.ambrosia.add.discord.util.BaseCommand;
import discord.util.dcf.slash.DCFSlashSubCommand;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandCasino extends BaseCommand {

    @Override
    public SlashCommandData getData() {
        return Commands.slash("casino", "Show stats about the casino");
    }

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
    }

    @Override
    public List<DCFSlashSubCommand> getSubCommands() {
        return List.of(new CommandCasinoProfits(), new CommandCasinoGames(), new CommandCasinoRatio());
    }

    @Override
    public boolean isOnlyManager() {
        return true;
    }
}
