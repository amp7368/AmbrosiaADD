package com.ambrosia.roulette.game.player.gui.line;

import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.gui.RoulettePlayerGui;
import com.ambrosia.roulette.game.player.gui.street.RouletteStreetPage;
import com.ambrosia.roulette.table.RouletteStreet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteSixLinePage extends RouletteStreetPage {

    public RouletteSixLinePage(RoulettePlayerGui parent) {
        super(parent);
        initButtons();
    }

    @Override
    protected void onStreetButton(RouletteStreet street) {
        new RouletteSixLineNeighborPage(getParent(), street);
    }

    @Override
    protected RouletteBetType<?> betType() {
        return RouletteBetType.SIX_LINE;
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = betEmbed();
        embed.setDescription("Select the street to place the first bet on");
        return betMessage(embed.build()).build();
    }
}
