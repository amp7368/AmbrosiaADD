package com.ambrosia.roulette.game.table.gui;

import com.ambrosia.roulette.img.RouletteImage;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteSpinWarmup extends DCFGuiPage<RouletteTableGui> {

    private static final MessageCreateData MESSAGE = new MessageCreateBuilder()
        .setEmbeds(new EmbedBuilder()
            .setTitle("Spin in 3... 2... 1...")
            .setImage(RouletteImage.IMAGE_STARTING_WHEEL)
            .build())
        .build();

    public RouletteSpinWarmup(RouletteTableGui rouletteTableGui) {
        super(rouletteTableGui);
    }

    @Override
    public MessageCreateData makeMessage() {
        return MESSAGE;
    }
}
