package com.ambrosia.roulette.game.table.gui;

import com.ambrosia.roulette.game.player.RoulettePlayerGame;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteTableStartPage extends DCFGuiPage<RouletteTableGui> {

    public static final String IMAGE_BETTING_TABLE = "https://static.voltskiya.com/assets/BettingTable.jpg";

    public RouletteTableStartPage(RouletteTableGui tableGui) {
        super(tableGui);
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Betting table");
        eb.setImage(IMAGE_BETTING_TABLE);
        for (RoulettePlayerGame player : parent.getGame().getPlayers()) {
            String status = player.getStatus().displayName();
            eb.addField(player.getPlayerName(), status, false);
        }
        return buildCreate(eb.build());
    }
}
