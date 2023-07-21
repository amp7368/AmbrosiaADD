package com.ambrosia.roulette.game.table.gui;

import com.ambrosia.add.discord.util.Emeralds;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.player.RoulettePlayerGame;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteTableBettingPage extends DCFGuiPage<RouletteTableGui> {

    public static final String IMAGE_BETTING_TABLE = "https://static.voltskiya.com/assets/BettingTable.jpg";

    public RouletteTableBettingPage(RouletteTableGui tableGui) {
        super(tableGui);
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Betting table");
        embed.setImage(IMAGE_BETTING_TABLE);
        for (RoulettePlayerGame player : parent.getGame().getPlayers()) {
            String playerTotal = Emeralds.message(player.getBetTotal(), true);
            String status = player.isBettingDisplay();
            String msg = "Total (%s) === Status **%s**".formatted(playerTotal, status);
            embed.addField(player.getPlayerName(), msg, false);
        }
        List<RouletteBet> bets = getParent().getGame().getLatestBets();
        StringBuilder description = new StringBuilder();
        for (int i = 0, max = Math.min(bets.size(), 20); i < max; i++) {
            RouletteBet bet = bets.get(i);
            Field betField = bet.toDiscordField();

            String playerName = bet.getPlayer().getPlayerName();
            String line = "***%s*** (%s) --- __%s__\n".formatted(
                betField.getName(),
                betField.getValue(),
                playerName);
            if (MessageEmbed.DESCRIPTION_MAX_LENGTH < description.length() + line.length())
                break;
            description.append(line);
        }
        embed.setDescription(description);

        return buildCreate(embed.build());
    }
}
