package com.ambrosia.roulette.game.player.gui;

import com.ambrosia.add.discord.util.AmbrosiaColor;
import com.ambrosia.roulette.game.player.RoulettePlayerGame;
import com.ambrosia.roulette.game.player.gui.line.RouletteSixLinePage;
import com.ambrosia.roulette.game.player.gui.split.RouletteSplitColPage;
import com.ambrosia.roulette.game.player.gui.split.RouletteSplitType;
import com.ambrosia.roulette.game.player.gui.straight.RouletteStraightUpPage;
import com.ambrosia.roulette.game.player.gui.straight.RouletteStraightUpType;
import com.ambrosia.roulette.game.player.gui.street.RouletteStreetPage;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RoulettePlayerMainPage extends DCFGuiPage<RoulettePlayerGui> {

    private static final Button STRAIGHT_UP_HIGH = Button.primary("straight_up_high", "Straight Up (High)");
    private static final Button STRAIGHT_UP_MIDDLE = Button.primary("straight_up_mid", "Straight Up (Mid)");
    private static final Button STRAIGHT_UP_LOW = Button.primary("straight_up_low", "Straight Up (Low)");
    private static final Button SPLIT_HIGH = Button.primary("split_high", "Split (High)");
    private static final Button SPLIT_MIDDLE = Button.primary("split_mid", "Split (Mid)");
    private static final Button SPLIT_LOW = Button.primary("split_low", "Split (Low)");
    private static final Button STREET = Button.primary("street", "Street");
    private static final Button CORNER_LOWER = Button.primary("corner_lower", "Corner (Lower)");
    private static final Button CORNER_UPPER = Button.primary("corner_upper", "Corner (Upper)");
    private static final Button SIX_LINE = Button.primary("six_line", "Six Line");
    private static final Button BASKET = Button.primary("basket", "Basket");
    private static final Button OUTSIDE = Button.primary("outside", "Outside");
    private static final Button CANCEL_BET = Button.danger("cancel", "Cancel");

    public RoulettePlayerMainPage(RoulettePlayerGui parent) {
        super(parent);
        this.initButtons();
    }

    private void initButtons() {
        registerButton(STRAIGHT_UP_HIGH.getId(), (e) -> new RouletteStraightUpPage(getParent(), RouletteStraightUpType.HIGH));
        registerButton(STRAIGHT_UP_MIDDLE.getId(), (e) -> new RouletteStraightUpPage(getParent(), RouletteStraightUpType.MIDDLE));
        registerButton(STRAIGHT_UP_LOW.getId(), (e) -> new RouletteStraightUpPage(getParent(), RouletteStraightUpType.LOW));

        registerButton(SPLIT_HIGH.getId(), (e) -> new RouletteSplitColPage(getParent(), RouletteSplitType.HIGH));
        registerButton(SPLIT_MIDDLE.getId(), (e) -> new RouletteSplitColPage(getParent(), RouletteSplitType.MIDDLE));
        registerButton(SPLIT_LOW.getId(), (e) -> new RouletteSplitColPage(getParent(), RouletteSplitType.LOW));

        registerButton(STREET.getId(), (e) -> new RouletteStreetPage(getParent()));

        registerButton(SIX_LINE.getId(), (e) -> new RouletteSixLinePage(getParent()));

        registerButton(CANCEL_BET.getId(), e -> getParent().doneBettingHook());
    }


    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(AmbrosiaColor.CASINO_COLOR);
        RoulettePlayerGame player = parent.getPlayer();

        String titleBet = player.getPartialBet().display();
        embed.setTitle("In-progress bet %s".formatted(titleBet));

        player.addBetsFieldSummary(embed);

        ActionRow row1 = ActionRow.of(STRAIGHT_UP_HIGH, STRAIGHT_UP_MIDDLE, STRAIGHT_UP_LOW);
        ActionRow row2 = ActionRow.of(SPLIT_HIGH, SPLIT_MIDDLE, SPLIT_LOW);
        ActionRow row3 = ActionRow.of(STREET, CORNER_LOWER, CORNER_UPPER);
        ActionRow row4 = ActionRow.of(SIX_LINE, BASKET, OUTSIDE);
        ActionRow actions = ActionRow.of(CANCEL_BET);
        return buildCreate(embed.build(), row1, row2, row3, row4, actions);
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
