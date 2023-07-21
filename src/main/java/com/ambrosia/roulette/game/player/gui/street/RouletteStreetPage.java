package com.ambrosia.roulette.game.player.gui.street;

import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.bet.types.RouletteBetStreet;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.gui.RoulettePlayerGui;
import com.ambrosia.roulette.game.player.gui.base.RouletteBetPage;
import com.ambrosia.roulette.table.RouletteSpace;
import com.ambrosia.roulette.table.RouletteStreet;
import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteStreetPage extends RouletteBetPage {

    public RouletteStreetPage(RoulettePlayerGui parent) {
        super(parent);
        initButtons();
    }

    @Override
    protected void initButtons() {
        super.initButtons();
        for (RouletteStreet street : Roulette.TABLE.streets()) {
            registerButton(streetButton(street).getId(), e -> {
                RouletteBet bet = getParent().getPlayer().finishBet((partialBet) -> {
                    RouletteBetStreet newBet = RouletteBetType.STREET.create(partialBet);
                    return newBet.finalizeStreets(street);
                });
                getParent().afterBetHook(bet);
            });
        }
    }

    @Override
    protected RouletteBetType<?> betType() {
        return RouletteBetType.STREET;
    }

    @Override
    protected List<List<ItemComponent>> betButtons() {
        List<Button> spaces = Roulette.TABLE.streets()
            .stream()
            .map(this::streetButton)
            .toList();
        return splitRows(spaces, 4);
    }

    private Button streetButton(RouletteStreet street) {
        List<String> spaces = Arrays.stream(street.getSpaces()).map(space -> space.display(false, false)).toList();
        String id = String.join("_", spaces);
        String name = "(%s)".formatted(String.join(", ", spaces));
        return Button.primary(id, name);
    }

    @Override
    protected void onDigitButton(RouletteSpace space) {

    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = betEmbed();
        embed.setDescription("Select the street to place a bet");
        return betMessage(embed.build()).build();
    }
}
