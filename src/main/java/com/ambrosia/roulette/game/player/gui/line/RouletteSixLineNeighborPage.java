package com.ambrosia.roulette.game.player.gui.line;

import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.impl.RouletteBetStreet;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.gui.RoulettePlayerGui;
import com.ambrosia.roulette.game.player.gui.street.RouletteStreetPage;
import com.ambrosia.roulette.table.RouletteStreet;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteSixLineNeighborPage extends RouletteStreetPage {

    private final RouletteStreet firstStreet;

    public RouletteSixLineNeighborPage(RoulettePlayerGui parent, RouletteStreet firstStreet) {
        super(parent);
        this.firstStreet = firstStreet;
    }

    @Override
    protected List<List<ItemComponent>> betButtons() {
        List<Button> spaces = Roulette.TABLE.streets()
            .stream()
            .filter(this.firstStreet::isNeighbor)
            .map(this::streetButton)
            .toList();
        return splitRows(spaces, 4);
    }

    @Override
    protected void onStreetButton(RouletteStreet secondStreet) {
        RouletteBet bet = getParent().getPlayer().finishBet((partialBet) -> {
            RouletteBetStreet newBet = RouletteBetType.SIX_LINE.create(partialBet);
            return newBet.finalizeStreets(this.firstStreet, secondStreet);
        });
        getParent().afterBetHook(bet);
    }

    @Override
    protected RouletteBetType<?> betType() {
        return RouletteBetType.SIX_LINE;
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = betEmbed();
        embed.setDescription("Select the neighboring street to place the second bet");
        embed.addField("Selected Street", "%s".formatted(firstStreet.display()), false);
        return betMessage(embed.build()).build();
    }
}
