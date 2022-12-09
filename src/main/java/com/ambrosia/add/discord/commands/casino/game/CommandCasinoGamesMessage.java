package com.ambrosia.add.discord.commands.casino.game;

import apple.utilities.util.Pretty;
import com.ambrosia.add.database.casino.game.CasinoGameCountByConclusion;
import com.ambrosia.add.database.casino.game.CasinoGamesCountByName;
import com.ambrosia.add.discord.DiscordBot;
import com.ambrosia.add.discord.util.Emeralds;
import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import java.util.Map.Entry;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class CommandCasinoGamesMessage extends DCFGuiPage<DCFGui> {

    private CasinoGamesCountByName total;

    public CommandCasinoGamesMessage(DCFGui dcfGui, CasinoGamesCountByName total) {
        super(dcfGui);
        this.total = total;
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(DiscordBot.CASINO_COLOR);
        embed.setTitle(Pretty.spaceEnumWords(total.name));
        for (Entry<String, CasinoGameCountByConclusion> conclusion : total.conclusionToGame.entrySet()) {
            String conclusionName = String.format("%s (%s games)", Pretty.spaceEnumWords(conclusion.getKey()),
                Pretty.commas(conclusion.getValue().count()));
            embed.addField(conclusionName, Emeralds.longMessage(conclusion.getValue().deltaWinnings(),false), false);
        }
        return buildCreate(embed.build(), ActionRow.of(this.btnPrev(), this.btnNext()));
    }
}
