package com.ambrosia.roulette.game.player.gui.straight;

import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.impl.RouletteBetSplit;
import com.ambrosia.roulette.game.bet.impl.RouletteBetStraightUp;
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

public class RouletteStraightUpPage extends RouletteBetPage {

    private final RouletteStraightUpType type;

    public RouletteStraightUpPage(RoulettePlayerGui roulettePlayerGui, RouletteStraightUpType type) {
        super(roulettePlayerGui);
        this.type = type;
        this.initButtons();
    }

    @Override
    protected void onDigitButton(RouletteSpace space) {
        RouletteBet bet = getParent().getPlayer().finishBet((partialBet) -> {
            RouletteBetStraightUp newBet = RouletteBetType.STRAIGHT_UP.create(partialBet);
            return newBet.finalizeDigits(List.of(space));
        });
        getParent().afterBetHook(bet);
    }

    @Override
    protected List<List<ItemComponent>> betButtons() {
        List<Button> spaces = Roulette.TABLE.getColumn(type.getColumn())
            .spacesStream()
            .map(this::digitButton)
            .toList();
        return splitRows(spaces, 4);
    }

    @Override
    protected RouletteBetType<RouletteBetSplit> betType() {
        return RouletteBetType.SPLIT;
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = betEmbed();
        embed.setDescription("Select the space to place a bet");
        embed.addField("Selected Column", "%s".formatted(type.name()), false);
        return betMessage(embed.build()).build();
    }

}
