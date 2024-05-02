package com.ambrosia.roulette.game.table.gui;

import com.ambrosia.roulette.img.RouletteImage;
import com.ambrosia.roulette.img.RouletteImage.SpinImage;
import com.ambrosia.roulette.table.RouletteSpace;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteSpinningPage extends DCFGuiPage<RouletteTableGui> {

    private final SpinImage spinImage;

    public RouletteSpinningPage(RouletteTableGui parent, RouletteSpace spinResult) {
        super(parent);
        spinImage = RouletteImage.spin(spinResult.digit());
    }

    public SpinImage getSpinImage() {
        return spinImage;
    }

    @Override
    public MessageCreateData makeMessage() {
        return new MessageCreateBuilder()
            .setEmbeds(new EmbedBuilder()
                .setImage(spinImage.gif())
                .build())
            .build();
    }
}
