package com.ambrosia.add.discord.commands.manager.casino.ratio;

import com.ambrosia.add.database.casino.CasinoQuery;
import com.ambrosia.add.database.casino.game.CasinoGamesCount;
import com.ambrosia.add.database.casino.game.CasinoGamesCountByName;
import com.ambrosia.add.discord.util.BaseSubCommand;
import discord.util.dcf.gui.base.gui.DCFGui;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandCasinoRatio extends BaseSubCommand {

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
        CasinoGamesCount games = new CasinoQuery().totalGames();
        DCFGui gui = new DCFGui(dcf, event::reply);
        for (CasinoGamesCountByName countForGame : games.gamesByName.values()) {
//            gui.addPage(new GuiCasinoRatio(gui, countForGame));
        }
        gui.send();
    }

    @Override
    public boolean isOnlyManager() {
        return true;
    }

    @Override
    public SubcommandData getData() {
        return new SubcommandData("ratio", "See the house edge");
    }
}
