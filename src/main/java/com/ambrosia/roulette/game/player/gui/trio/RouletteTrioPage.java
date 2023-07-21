package com.ambrosia.roulette.game.player.gui.trio;

import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.impl.RouletteBetBasket;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.gui.RoulettePlayerGui;
import com.ambrosia.roulette.game.player.gui.base.RouletteBetPage;
import com.ambrosia.roulette.table.RouletteSpace;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteTrioPage extends RouletteBetPage {

    private final RouletteTrio LOW_TRIO = new RouletteTrio(new int[]{0, 1, 2});
    private final RouletteTrio HIGH_TRIO = new RouletteTrio(new int[]{0, 2, 3});

    public RouletteTrioPage(RoulettePlayerGui parent) {
        super(parent);
        initButtons();
    }

    protected void initButtons() {
        super.initButtons();
        registerButton(HIGH_TRIO.id(), e -> onTrioButton(HIGH_TRIO));
        registerButton(LOW_TRIO.id(), e -> onTrioButton(LOW_TRIO));
    }

    private void onTrioButton(RouletteTrio trio) {
        RouletteBet bet = getParent().getPlayer().finishBet((partialBet) -> {
            RouletteBetBasket newBet = RouletteBetType.TRIO.create(partialBet);
            return newBet.finalizeDigits(List.of(trio.spaces()));
        });
        getParent().afterBetHook(bet);
    }

    @Override
    protected RouletteBetType<?> betType() {
        return RouletteBetType.TRIO;
    }

    @Override
    protected List<List<ItemComponent>> betButtons() {
        return new ArrayList<>() {{
            add(List.of(LOW_TRIO.button(), HIGH_TRIO.button()));
        }};
    }

    @Override
    protected void onDigitButton(RouletteSpace space) {

    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = betEmbed();
        embed.setDescription("Select the trio to place a bet");
        return betMessage(embed.build()).build();
    }

    private record RouletteTrio(RouletteSpace[] spaces) {

        public RouletteTrio(int[] spaces) {
            this(Arrays.stream(spaces)
                .mapToObj(Roulette.TABLE::getSpace)
                .toArray(RouletteSpace[]::new));
        }

        public String display() {
            return "(%s)".formatted(String.join(", ", spacesDisplay()));
        }

        private List<String> spacesDisplay() {
            return Arrays.stream(spaces())
                .map(space -> space.display(false, false))
                .toList();
        }

        public String id() {
            return String.join("_", spacesDisplay());
        }

        public Button button() {
            return Button.primary(id(), display());
        }
    }
}
