package com.ambrosia.roulette.game.player.gui.bet;

import com.ambrosia.roulette.game.player.gui.RoulettePlayerGui;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteStraightUpPage extends DCFGuiPage<RoulettePlayerGui> {

    private final RouletteStraightUpType type;

    public RouletteStraightUpPage(RoulettePlayerGui roulettePlayerGui, RouletteStraightUpType type) {
        super(roulettePlayerGui);
        this.type = type;
        editMessage();
    }

    
    @Override
    public MessageCreateData makeMessage() {
        return buildCreate("Straight up");
    }
}
