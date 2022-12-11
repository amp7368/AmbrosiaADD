package com.ambrosia.add.discord.commands.casino.game;

import apple.utilities.util.Pretty;
import com.ambrosia.add.database.casino.game.CasinoGameCountByConclusion;
import com.ambrosia.add.database.casino.game.CasinoGamesCountByName;
import com.ambrosia.add.discord.DiscordBot;
import com.ambrosia.add.discord.util.Emeralds;
import com.github.AndrewAlbizati.Blackjack;
import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

public class GuiCasinoGames extends DCFGuiPage<DCFGui> {

    private String authorName;
    private String authorIcon;
    private CasinoGamesCountByName total;

    public GuiCasinoGames(DCFGui dcfGui, String authorName, String authorIcon, CasinoGamesCountByName total) {
        super(dcfGui);
        this.authorName = authorName;
        this.authorIcon = authorIcon;
        this.total = total;
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(DiscordBot.CASINO_COLOR);
        embed.setTitle(Pretty.spaceEnumWords(total.name));
        embed.setAuthor(this.authorName, this.authorIcon);
        embed.addField(conclusionField("Total", total.count, Math.abs(total.deltaWinnings)));
        embed.addField(total.houseEdgeDisplay());
        embed.addBlankField(true);
        boolean isInset = false;
        List<Entry<String, CasinoGameCountByConclusion>> sortedConclusions = new ArrayList<>(total.conclusionToGame.entrySet());
        if (total.name.equalsIgnoreCase(Blackjack.GAME_NAME)) {
            sortedConclusions.sort(Entry.comparingByKey(CasinoGameOrdering.comparing(total.name)));
        }
        for (Entry<String, CasinoGameCountByConclusion> conclusionEntry : sortedConclusions) {
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


    @NotNull
    public Field conclusionField(String nameRaw, long countRaw, long totalRaw) {
        String conclusionName = String.format("%s (%s games)", Pretty.spaceEnumWords(nameRaw), Pretty.commas(countRaw));
        String emeralds = Emeralds.message(totalRaw, Integer.MAX_VALUE, true);
        return new Field(conclusionName, emeralds, true);
    }
}
