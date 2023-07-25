package com.ambrosia.roulette.game.player.gui.outside;

import com.ambrosia.roulette.game.bet.impl.outside.RouletteBetColumnType;
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

public class RouletteColumnPage extends RouletteBetPage {

    public RouletteColumnPage(RoulettePlayerGui parent) {
        super(parent);
        this.initButtons();
    }

    @Override
    protected void initButtons() {
        super.initButtons();
        for (RouletteBetColumnType dozen : RouletteBetColumnType.values()) {
            registerButton(this.colButton(dozen).getId(), e -> finishBet(dozen));
        }
    }

    private void finishBet(RouletteBetColumnType dozen) {
        RouletteBet bet = getParent().getPlayer()
            .finishBet(bet1 -> RouletteBetType.COLUMN.create(bet1)
                .finalizeColumn(dozen));
        getParent().afterBetHook(bet);
    }

    @Override
    protected RouletteBetType<?> betType() {
        return RouletteBetType.COLUMN;
    }

    @Override
    protected List<List<ItemComponent>> betButtons() {
        List<ItemComponent> buttons = Arrays.stream(RouletteBetColumnType.values())
            .sorted(Comparator.comparingInt(RouletteBetColumnType::id))
            .<ItemComponent>map(this::colButton)
            .toList();
        return List.of(buttons);
    }

    @NotNull
    private Button colButton(RouletteBetColumnType dozen) {
        return Button.primary(String.valueOf(dozen.id()), dozen.display());
    }

    @Override
    protected void onDigitButton(RouletteSpace space) {
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = betEmbed();
        embed.setDescription("Select the column to place a bet");
        return betMessage(embed.build()).build();
    }
}
