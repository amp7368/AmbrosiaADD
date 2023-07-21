package com.ambrosia.roulette.game.player.gui.bet;

import com.ambrosia.add.discord.util.AmbrosiaColor;
import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.impl.RouletteBetStraightUp;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.RoulettePlayerGame;
import com.ambrosia.roulette.game.player.gui.RoulettePlayerGui;
import com.ambrosia.roulette.table.RouletteSpace;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteStraightUpPage extends DCFGuiPage<RoulettePlayerGui> {

    private static final Button CANCEL_BUTTON = Button.danger("cancel", "Cancel");
    private final RouletteStraightUpType type;

    public RouletteStraightUpPage(RoulettePlayerGui roulettePlayerGui, RouletteStraightUpType type) {
        super(roulettePlayerGui);
        this.type = type;
        this.initButtons();
        getParent().addSubPage(this);
    }

    private void initButtons() {
        registerButton(CANCEL_BUTTON.getId(), e -> getParent().clearSubPages());
        for (RouletteSpace space : Roulette.TABLE.spaces(false)) {
            registerButton(digitButton(space).getId(), (e) -> {
                RoulettePlayerGame player = getParent().getPlayer();
                RouletteBet bet = player.finishBet((partialBet) -> {
                    RouletteBetStraightUp newBet = RouletteBetType.STRAIGHT_UP.create(partialBet);
                    return newBet.finalizeDigits(List.of(space));
                });
                getParent().afterBetHook(bet);
            });
        }
    }

    private List<List<ItemComponent>> buttons() {
        RouletteSpace[] spaces = Roulette.TABLE.getColumn(type.getColumn()).getSpaces();
        List<List<ItemComponent>> buttons = new ArrayList<>();
        List<ItemComponent> row = new ArrayList<>(4);
        for (int i = 0, spacesLength = spaces.length; i < spacesLength; i++) {
            RouletteSpace space = spaces[i];
            row.add(digitButton(space));
            if ((i + 1) % 4 == 0) {
                buttons.add(row);
                row = new ArrayList<>(4);
            }
        }
        return buttons;
    }

    private Button digitButton(RouletteSpace space) {
        return Button.primary("digit_" + space.digit(), String.valueOf(space.digit()));
    }


    @Override
    public MessageCreateData makeMessage() {
        List<List<ItemComponent>> buttons = buttons();
        buttons.add(List.of(CANCEL_BUTTON));

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(AmbrosiaColor.CASINO_COLOR);
        embed.setTitle("Straight up - (Bet %s)".formatted(getParent().getPlayer().getPartialBet().display()));
        embed.setDescription("Select the space to place a bet");
        embed.addField("Selected", "%s column".formatted(type.name()), false);

        MessageCreateBuilder msg = new MessageCreateBuilder();
        msg.setEmbeds(embed.build());
        buttons.forEach(msg::addActionRow);
        return msg.build();
    }
}
