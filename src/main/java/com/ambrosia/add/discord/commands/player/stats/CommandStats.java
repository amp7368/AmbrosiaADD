package com.ambrosia.add.discord.commands.player.stats;

import com.ambrosia.add.database.casino.CasinoQuery;
import com.ambrosia.add.database.casino.game.CasinoGamesCount;
import com.ambrosia.add.database.casino.game.CasinoGamesCountByName;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.discord.DiscordBot;
import com.ambrosia.add.discord.commands.manager.casino.game.GuiCasinoGames;
import com.ambrosia.add.discord.util.BaseCommand;
import discord.util.dcf.gui.base.gui.DCFGui;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandStats extends BaseCommand {

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
        ClientEntity client = ClientStorage.get().findByDiscord(event.getUser().getIdLong());
        if (client == null) {
            this.errorRegisterWithStaff(event);
            return;
        }
        CasinoGamesCount games = new CasinoQuery().totalGames();
        DCFGui gui = new DCFGui(dcf, event::reply);
        for (CasinoGamesCountByName countForGame : games.gamesByName.values())
            gui.addPage(new GuiCasinoGames(gui, client.displayName, DiscordBot.SELF_USER_AVATAR, countForGame));
        gui.send();
    }

    @Override
    public boolean isOnlyManager() {
        return true;
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("stats", "Show more of your stats");
    }
}
