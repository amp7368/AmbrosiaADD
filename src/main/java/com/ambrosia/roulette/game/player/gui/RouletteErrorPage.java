package com.ambrosia.roulette.game.player.gui;

import com.ambrosia.add.discord.util.AmbrosiaColor;
import com.ambrosia.add.discord.util.Emeralds;
import com.ambrosia.roulette.game.bet.RouletteBetSubCommand;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteErrorPage extends DCFGuiPage<RoulettePlayerGui> {

    private static final Button OKAY_BUTTON = Button.success("okay", "Okay");

    public RouletteErrorPage(RoulettePlayerGui parent) {
        super(parent);
        registerButton(OKAY_BUTTON.getId(), e -> this.parent.popSubPage());
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(AmbrosiaColor.ERROR);
        embed.setTitle("Error!");
        String desc = "Cannot bet above %s on the same space!"
            .formatted(Emeralds.message(RouletteBetSubCommand.ONE_HAND_MAX_BET, false));
        embed.setDescription(desc);
        MessageCreateBuilder msg = new MessageCreateBuilder();
        msg.setEmbeds(embed.build());
        msg.addActionRow(OKAY_BUTTON);
        return msg.build();
    }
}
