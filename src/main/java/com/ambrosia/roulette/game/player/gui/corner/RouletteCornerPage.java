package com.ambrosia.roulette.game.player.gui.corner;

import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.impl.RouletteBetCorner;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.gui.RoulettePlayerGui;
import com.ambrosia.roulette.game.player.gui.base.RouletteBetPage;
import com.ambrosia.roulette.table.RouletteSpace;
import com.ambrosia.roulette.table.RouletteTable;
import java.util.List;
import java.util.stream.Stream;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

public class RouletteCornerPage extends RouletteBetPage {

    private final RouletteCornerType type;

    public RouletteCornerPage(RoulettePlayerGui parent, RouletteCornerType type) {
        super(parent);
        this.type = type;
        initButtons();
    }

    @Override
    protected void initButtons() {
        super.initButtons();
        for (RouletteCorner corner : corners()) {
            registerButton(corner.id(), e -> onCornerButton(corner));
        }
    }

    private void onCornerButton(RouletteCorner corner) {
        RouletteBet bet = getParent().getPlayer().finishBet((partialBet) -> {
            RouletteBetCorner newBet = RouletteBetType.CORNER.create(partialBet);
            return newBet.finalizeDigits(corner.spaces().toList());
        });
        getParent().afterBetHook(bet);
    }

    private List<RouletteCorner> corners() {
        return Roulette.TABLE.spacesStream(false)
            .filter(this::isColumn)
            .map(RouletteSpace::digit)
            .map(RouletteCorner::new)
            .toList();
    }

    private boolean isColumn(RouletteSpace space) {
        return space.col() == type.cornerRow() &&
            space.row() != RouletteTable.MAX_ROWS - 1;
    }


    @Override
    protected RouletteBetType<?> betType() {
        return RouletteBetType.CORNER;
    }

    @Override
    protected List<List<ItemComponent>> betButtons() {
        List<Button> buttons = corners().stream()
            .map(RouletteCorner::button)
            .toList();
        return splitRows(buttons, 4);
    }

    @Override
    protected void onDigitButton(RouletteSpace space) {

    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = betEmbed();
        embed.setDescription("Select the corner to place a bet");
        return betMessage(embed.build()).build();
    }

    private record RouletteCorner(RouletteSpace lowest, RouletteSpace low, RouletteSpace high, RouletteSpace highest) {

        public RouletteCorner(int digit) {
            this(Roulette.TABLE.getSpace(digit),
                Roulette.TABLE.getSpace(digit + 1),
                Roulette.TABLE.getSpace(digit + 3),
                Roulette.TABLE.getSpace(digit + 4));
        }

        public String display() {
            return "(%s)".formatted(String.join(", ", spacesDisplay()));
        }

        private List<String> spacesDisplay() {
            return spaces()
                .map(space -> space.display(false, false))
                .toList();
        }

        @NotNull
        private Stream<RouletteSpace> spaces() {
            return Stream.of(lowest, low, high, highest);
        }

        public String id() {
            return String.join("_", spacesDisplay());
        }

        public Button button() {
            return Button.primary(id(), display());
        }
    }
}
