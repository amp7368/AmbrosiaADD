package com.ambrosia.roulette.game.player.gui.outside;

import com.ambrosia.roulette.game.bet.impl.outside.RouletteBetDozenType;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.gui.RoulettePlayerGui;
import com.ambrosia.roulette.game.player.gui.base.RouletteBetPage;
import com.ambrosia.roulette.table.RouletteSpace;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

public class RouletteDozenPage extends RouletteBetPage {

    public RouletteDozenPage(RoulettePlayerGui roulettePlayerGui) {
        super(roulettePlayerGui);
        this.initButtons();
    }

    @Override
    protected void initButtons() {
        super.initButtons();
        for (RouletteBetDozenType dozen : RouletteBetDozenType.values()) {
            registerButton(this.dozenButton(dozen).getId(), e -> finishBet(dozen));
        }
    }

    private void finishBet(RouletteBetDozenType dozen) {
        RouletteBet bet = getParent().getPlayer()
            .finishBet(bet1 -> RouletteBetType.DOZEN.create(bet1)
                .finalizeDozen(dozen));
        getParent().afterBetHook(bet);
    }

    @Override
    protected RouletteBetType<?> betType() {
        return RouletteBetType.DOZEN;
    }

    @Override
    protected List<List<ItemComponent>> betButtons() {
        List<ItemComponent> buttons = Arrays.stream(RouletteBetDozenType.values())
            .sorted(Comparator.comparingInt(RouletteBetDozenType::id))
            .<ItemComponent>map(this::dozenButton)
            .toList();
        return List.of(buttons);
    }

    @NotNull
    private Button dozenButton(RouletteBetDozenType dozen) {
        return Button.primary(String.valueOf(dozen.id()), dozen.range());
    }

    @Override
    protected void onDigitButton(RouletteSpace space) {
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = betEmbed();
        embed.setDescription("Select the dozen to place a bet");
        return betMessage(embed.build()).build();
    }
}
