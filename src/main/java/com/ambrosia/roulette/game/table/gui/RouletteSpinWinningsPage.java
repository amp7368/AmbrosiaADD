package com.ambrosia.roulette.game.table.gui;

import com.ambrosia.roulette.game.player.RoulettePlayerGame;
import com.ambrosia.roulette.game.player.RoulettePlayerWinnings;
import com.ambrosia.roulette.game.table.RouletteGame;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteSpinWinningsPage extends DCFGuiPage<RouletteTableGui> {

    private final String spinImage;

    public RouletteSpinWinningsPage(RouletteTableGui parent, String spinImage) {
        super(parent);
        this.spinImage = spinImage;
    }

    @Override
    public MessageCreateData makeMessage() {
        String title = "Landed on %s!".formatted(
            getGame().getSpinResult().display(false, true));
        String personalBets = "/roulette pstats game_id:%d".formatted(getGame().getId());
        EmbedBuilder embed = new EmbedBuilder()
            .setAuthor(personalBets)
            .setTitle(title)
            .setImage(spinImage);
        this.getGame().getPlayers()
            .stream()
            .map(RoulettePlayerGame::getWinnings)
            .map(RoulettePlayerWinnings::shortSummaryField)
            .forEach(embed::addField);
        return new MessageCreateBuilder()
            .setEmbeds(embed.build())
            .build();
    }

    private RouletteGame getGame() {
        return getParent().getGame();
    }
}
