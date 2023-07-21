package com.ambrosia.roulette.game.player.gui;

import com.ambrosia.add.discord.util.AmbrosiaColor;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteDoneBettingPage extends DCFGuiPage<RoulettePlayerGui> {

    public RouletteDoneBettingPage(RoulettePlayerGui parent) {
        super(parent);
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(AmbrosiaColor.CASINO_COLOR);
        embed.setTitle("Done betting!");
        embed.setDescription("If you change your mind, you can use `/roulette bet` to bet again before the wheel starts spinning!");
        Member discord = parent.getPlayer().getDiscord();
        embed.setAuthor(discord.getEffectiveName(), null, discord.getEffectiveAvatarUrl());
        return buildCreate(embed.build());
    }
}
