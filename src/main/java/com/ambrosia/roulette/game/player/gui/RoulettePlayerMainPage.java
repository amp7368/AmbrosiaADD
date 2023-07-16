package com.ambrosia.roulette.game.player.gui;

import com.ambrosia.add.discord.util.Emeralds;
import com.ambrosia.roulette.game.bet.RouletteBet;
import com.ambrosia.roulette.game.player.RoulettePlayerGame;
import com.ambrosia.roulette.game.player.gui.bet.RouletteStraightUpPage;
import com.ambrosia.roulette.game.player.gui.bet.RouletteStraightUpType;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RoulettePlayerMainPage extends DCFGuiPage<RoulettePlayerGui> {

    private static final Button STRAIGHT_UP_HIGH = Button.primary("straight_up_high", "Straight Up (High)");
    private static final Button STRAIGHT_UP_LOW = Button.primary("straight_up_low", "Straight Up (Low)");
    private static final Button SPLIT_HIGH = Button.primary("split_high", "Split (High)");
    private static final Button SPLIT_LOW = Button.primary("split_low", "Split (Low)");
    private static final Button STREET = Button.primary("street", "Split (Low)");
    private static final Button CORNER_LOWER = Button.primary("corner_lower", "Corner (Lower)");
    private static final Button CORNER_UPPER = Button.primary("corner_upper", "Corner (Upper)");
    private static final Button SIX_LINE = Button.primary("six_line", "Six Line");
    private static final Button BASKET = Button.primary("basket", "Basket");
    private static final Button OUTSIDE = Button.primary("outside", "Outside");

    public RoulettePlayerMainPage(RoulettePlayerGui parent) {
        super(parent);
        this.initButtons();
    }

    private void initButtons() {
        registerButton(STRAIGHT_UP_HIGH.getId(), (e) -> new RouletteStraightUpPage(getParent(), RouletteStraightUpType.HIGH));
        registerButton(STRAIGHT_UP_LOW.getId(), (e) -> new RouletteStraightUpPage(getParent(), RouletteStraightUpType.LOW));
    }


    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder eb = new EmbedBuilder();
        RoulettePlayerGame player = parent.getPlayer();

        String titleBet = Emeralds.message(player.getPartialBet().getAmount(), true);
        eb.setTitle("In-progress bet %s".formatted(titleBet));

        player.getBets()
            .stream()
            .map(RouletteBet::toDiscordField)
            .forEach(eb::addField);

        ActionRow row1 = ActionRow.of(STRAIGHT_UP_HIGH, STRAIGHT_UP_LOW);
        ActionRow row2 = ActionRow.of(SPLIT_HIGH, SPLIT_LOW, STREET);
        ActionRow row3 = ActionRow.of(CORNER_LOWER, CORNER_UPPER);
        ActionRow row4 = ActionRow.of(SIX_LINE, BASKET, OUTSIDE);
        return buildCreate(eb.build(), row1, row2, row3, row4);
    }

    @Override
    public Button btnPrev() {
        return Button.secondary("prev", "\u2B05\uFE0F");
    }

    @Override
    public Button btnNext() {
        return Button.secondary("next", "\u27A1\uFE0F");
    }
}
