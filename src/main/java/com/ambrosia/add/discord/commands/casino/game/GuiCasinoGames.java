package com.ambrosia.add.discord.commands.casino.game;

import apple.utilities.util.Pretty;
import com.ambrosia.add.database.casino.game.CasinoGameCountByConclusion;
import com.ambrosia.add.database.casino.game.CasinoGamesCountByName;
import com.ambrosia.add.discord.DiscordBot;
import com.ambrosia.add.discord.util.Emeralds;
import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map.Entry;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

public class GuiCasinoGames extends DCFGuiPage<DCFGui> {

    private CasinoGamesCountByName total;

    public GuiCasinoGames(DCFGui dcfGui, CasinoGamesCountByName total) {
        super(dcfGui);
        this.total = total;
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(DiscordBot.CASINO_COLOR);
        embed.setTitle(Pretty.spaceEnumWords(total.name));
        embed.addField(conclusionField("Total", total.count, Math.abs(total.deltaWinnings)));
        embed.addField(houseEdge(total));
        embed.addBlankField(true);
        boolean isInset = false;
        for (Entry<String, CasinoGameCountByConclusion> conclusionEntry : total.conclusionToGame.entrySet()) {
            CasinoGameCountByConclusion conclusion = conclusionEntry.getValue();
            embed.addField(conclusionField(conclusionEntry.getKey(), conclusion.count(), conclusion.deltaWinningsAbs()));
            if (isInset) isInset = false;
            else {
                embed.addBlankField(true);
                isInset = true;
            }
        }
        return buildCreate(embed.build(), ActionRow.of(this.btnPrev(), this.btnNext()));
    }

    private Field houseEdge(CasinoGamesCountByName total) {
        if (total.totalExchanged == 0) return new Field("House Edge", "No data", true);
        BigDecimal deltaWinnings = BigDecimal.valueOf(-total.deltaWinnings); // we're House, so flip the sign
        BigDecimal totalExchanged = BigDecimal.valueOf(total.totalExchanged);
        BigDecimal houseWins = deltaWinnings.divide(totalExchanged, Double.SIZE, RoundingMode.HALF_UP).multiply(BigDecimal.TEN)
            .multiply(BigDecimal.TEN);
        String edgeMessage = String.format("%.3f%%", houseWins.doubleValue());
        return new Field("House Edge", edgeMessage, true);
    }

    @NotNull
    public Field conclusionField(String nameRaw, long countRaw, long totalRaw) {
        String conclusionName = String.format("%s (%s games)", Pretty.spaceEnumWords(nameRaw), Pretty.commas(countRaw));
        String emeralds = Emeralds.longMessage(totalRaw, false);
        return new Field(conclusionName, emeralds, true);
    }
}
