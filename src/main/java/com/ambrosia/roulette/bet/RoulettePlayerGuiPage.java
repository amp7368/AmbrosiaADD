package com.ambrosia.roulette.bet;

import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RoulettePlayerGuiPage extends DCFGuiPage<RoulettePlayerGui> {

    private static final Button STRAIGHT_UP = Button.primary("straight_up", "Straight Up");
    private static final Button SPLIT = Button.primary("split", "Split");

    public RoulettePlayerGuiPage(RoulettePlayerGui parent) {
        super(parent);
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Bet");
        ActionRow primary = ActionRow.of(STRAIGHT_UP, SPLIT);
        ActionRow navigation = ActionRow.of(btnPrev(), btnNext());
        return buildCreate(eb.build(), primary, navigation);
    }

    @Override
    public Button btnPrev() {
        return Button.secondary("prev", ":arrow_left:");
    }

    @Override
    public Button btnNext() {
        return Button.secondary("next", ":arrow_right:");
    }
}
