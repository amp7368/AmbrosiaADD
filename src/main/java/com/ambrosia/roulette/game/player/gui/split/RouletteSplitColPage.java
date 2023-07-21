package com.ambrosia.roulette.game.player.gui.split;

import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.impl.RouletteBetSplit;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.gui.RoulettePlayerGui;
import com.ambrosia.roulette.game.player.gui.base.RouletteBetPage;
import com.ambrosia.roulette.table.RouletteSpace;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteSplitColPage extends RouletteBetPage {

    private final RouletteSplitType type;

    public RouletteSplitColPage(RoulettePlayerGui parent, RouletteSplitType type) {
        super(parent);
        this.type = type;
        this.initButtons();
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
    protected void onDigitButton(RouletteSpace space) {
        new RouletteSplitNeighborPage(getParent(), type, space);
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = betEmbed();
        embed.setDescription("Select the space to place the first split");
        embed.addField("Selected Column", "%s".formatted(type.name()), false);
        return betMessage(embed.build()).build();
    }
}
