package com.ambrosia.roulette.game.player.gui.split;

import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.impl.RouletteBetSplit;
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

public class RouletteSplitNeighborPage extends RouletteBetPage {

    private final RouletteSplitType type;
    private final RouletteSpace firstSpace;

    public RouletteSplitNeighborPage(RoulettePlayerGui parent, RouletteSplitType type, RouletteSpace firstSpace) {
        super(parent);
        this.type = type;
        this.firstSpace = firstSpace;
        this.initButtons();
    }

    @Override
    protected List<List<ItemComponent>> betButtons() {
        List<Button> spaces = Roulette.TABLE.spacesStream(true)
            .filter(firstSpace::isNeighbor)
            .map(this::digitButton)
            .toList();
        return splitRows(spaces, 4);
    }

    @Override
    protected void onDigitButton(RouletteSpace secondSpace) {
        RouletteBet bet = getParent().getPlayer().finishBet((partialBet) -> {
            RouletteBetSplit newBet = RouletteBetType.SPLIT.create(partialBet);
            return newBet.finalizeDigits(List.of(this.firstSpace, secondSpace));
        });
        getParent().afterBetHook(bet);
    }

    @Override
    protected boolean includeBackButton() {
        return true;
    }

    @Override
    protected RouletteBetType<RouletteBetSplit> betType() {
        return RouletteBetType.SPLIT;
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = betEmbed();
        embed.setDescription("Select the neighboring space to place the second split");
        embed.addField("Selected Space", "%s".formatted(firstSpace.display(true, true)), false);
        embed.addField("Selected Column", "%s".formatted(type.name()), false);
        return betMessage(embed.build()).build();
    }
}
