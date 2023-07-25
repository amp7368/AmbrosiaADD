package com.ambrosia.roulette.game.player.gui.outside;

import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.gui.RoulettePlayerGui;
import com.ambrosia.roulette.game.player.gui.base.RouletteBetPage;
import com.ambrosia.roulette.table.RouletteSpace;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteOutsidePage extends RouletteBetPage {

    private static final Button RED_BUTTON = Button.primary("red", "Red");
    private static final Button BLACK_BUTTON = Button.primary("black", "Black");
    private static final Button EVEN_BUTTON = Button.primary("even", "Even");
    private static final Button ODD_BUTTON = Button.primary("odd", "Odd");
    private static final Button HIGH_BUTTON = Button.primary("high", "High (19-36)");
    private static final Button LOW_BUTTON = Button.primary("low", "Low (1-18)");
    private static final Button COLUMN_BUTTON = Button.primary("column", "Column");
    private static final Button DOZEN_BUTTON = Button.primary("dozen", "Dozen");

    public RouletteOutsidePage(RoulettePlayerGui parent) {
        super(parent);
        this.initButtons();
    }

    @Override
    protected void initButtons() {
        super.initButtons();
        this.registerButton(RED_BUTTON.getId(), e -> finishBet(RouletteBetType.RED));
        this.registerButton(BLACK_BUTTON.getId(), e -> finishBet(RouletteBetType.BLACK));
        this.registerButton(ODD_BUTTON.getId(), e -> finishBet(RouletteBetType.ODD));
        this.registerButton(EVEN_BUTTON.getId(), e -> finishBet(RouletteBetType.EVEN));
        this.registerButton(HIGH_BUTTON.getId(), e -> finishBet(RouletteBetType.HIGH));
        this.registerButton(LOW_BUTTON.getId(), e -> finishBet(RouletteBetType.LOW));
        this.registerButton(DOZEN_BUTTON.getId(), e -> new RouletteDozenPage(getParent()));
        this.registerButton(COLUMN_BUTTON.getId(), e -> new RouletteColumnPage(getParent()));
    }

    private void finishBet(RouletteBetType<?> betType) {
        RouletteBet bet = getParent().getPlayer().finishBet(betType::create);
        getParent().afterBetHook(bet);
    }

    @Override
    protected String betTypeDisplay() {
        return "Outside";
    }

    @Override
    protected RouletteBetType<?> betType() {
        return null;
    }

    @Override
    protected List<List<ItemComponent>> betButtons() {
        List<ItemComponent> row1 = List.of(RED_BUTTON, BLACK_BUTTON);
        List<ItemComponent> row2 = List.of(ODD_BUTTON, EVEN_BUTTON);
        List<ItemComponent> row3 = List.of(LOW_BUTTON, HIGH_BUTTON);
        List<ItemComponent> row4 = List.of(COLUMN_BUTTON, DOZEN_BUTTON);
        return List.of(row1, row2, row3, row4);
    }

    @Override
    protected void onDigitButton(RouletteSpace space) {
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = betEmbed();
        embed.setDescription("Select an outside bet");
        return betMessage(embed.build()).build();
    }
}
