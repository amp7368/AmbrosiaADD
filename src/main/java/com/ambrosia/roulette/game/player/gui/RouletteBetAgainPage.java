package com.ambrosia.roulette.game.player.gui;

import com.ambrosia.add.discord.util.AmbrosiaColor;
import com.ambrosia.add.discord.util.Emeralds;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteBetAgainPage extends DCFGuiPage<RoulettePlayerGui> {

    private static final Button DONE_BETTING = Button.danger("done", "Done Betting");
    private final List<BetAgainButton> betAgainButtons = new ArrayList<>();

    public RouletteBetAgainPage(RoulettePlayerGui parent, RouletteBet bet) {
        super(parent);
        int emeralds = bet.getAmount();
        betAgainButtons.add(new BetAgainButton(emeralds, 0, 0.5));
        betAgainButtons.add(new BetAgainButton(emeralds, 1, 1));
        betAgainButtons.add(new BetAgainButton(emeralds, 2, 1.5));
        betAgainButtons.add(new BetAgainButton(emeralds, 3, 2));
        this.initButtons();
    }

    private void initButtons() {
        betAgainButtons.forEach(this::registerButton);
    }

    private void registerButton(BetAgainButton btn) {
        registerButton(btn.btnId(), e -> {
            getParent().getPlayer().startBet(btn.btnAmount());
            getParent().clearSubPages();
        });
        registerButton(DONE_BETTING.getId(), e -> getParent().doneBettingHook());
    }


    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(AmbrosiaColor.CASINO_COLOR);
        embed.setTitle("Bet again?");
        embed.setDescription("Confirmed latest bet! \u2705");
        getParent().getPlayer().addBetsFieldSummary(embed);
        MessageCreateBuilder msg = new MessageCreateBuilder();
        msg.setEmbeds(embed.build());
        msg.addActionRow(betAgainButtons.stream().map(BetAgainButton::btn).toList());
        msg.addActionRow(DONE_BETTING);
        return msg.build();
    }

    private record BetAgainButton(int emeralds, int index, double multiplier) {

        public Button btn() {
            String emeralds = Emeralds.message(btnAmount(), false);
            String display = "Bet %s (%1.1fX)".formatted(emeralds, multiplier());
            String id = btnId();
            return Button.success(id, display);
        }

        public String btnId() {
            return "bet_again_%d".formatted(this.index);
        }

        public int btnAmount() {
            return (int) Math.ceil(emeralds * multiplier);
        }

    }
}
