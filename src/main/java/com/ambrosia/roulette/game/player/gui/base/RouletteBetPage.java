package com.ambrosia.roulette.game.player.gui.base;

import com.ambrosia.add.discord.util.AmbrosiaColor;
import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import com.ambrosia.roulette.game.player.gui.RoulettePlayerGui;
import com.ambrosia.roulette.table.RouletteSpace;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

public abstract class RouletteBetPage extends DCFGuiPage<RoulettePlayerGui> {

    protected static final Button CANCEL_BUTTON = Button.danger("cancel", "Cancel");
    private static final Button BACK_BUTTON = Button.danger("pop_page", "Back");

    public RouletteBetPage(RoulettePlayerGui roulettePlayerGui) {
        super(roulettePlayerGui);
        getParent().addSubPage(this);
    }

    protected void initButtons() {
        registerButton(CANCEL_BUTTON.getId(), e -> getParent().clearSubPages());
        registerButton(BACK_BUTTON.getId(), e -> getParent().popSubPage());
        for (RouletteSpace space : Roulette.TABLE.spaces(false)) {
            registerButton(digitButton(space).getId(), e -> onDigitButton(space));
        }
    }

    protected final Button digitButton(RouletteSpace space) {
        return Button.primary("digit_" + space.digit(), String.valueOf(space.digit()));
    }

    @NotNull
    protected final List<List<ItemComponent>> splitRows(List<Button> buttons, int columns) {
        List<List<ItemComponent>> rows = new ArrayList<>();
        List<ItemComponent> row = new ArrayList<>(4);
        for (int i = 0, spacesLength = buttons.size(); i < spacesLength; i++) {
            row.add(buttons.get(i));
            if ((i + 1) % columns == 0) {
                rows.add(row);
                row = new ArrayList<>(4);
            }
        }
        if (!row.isEmpty()) rows.add(row);
        return rows;
    }

    @NotNull
    protected final EmbedBuilder betEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(AmbrosiaColor.CASINO_COLOR);
        embed.setTitle("%s - (Bet %s)".formatted(betTypeDisplay(), getParent().getPlayer().getPartialBet().display()));
        return embed;
    }

    protected String betTypeDisplay() {
        return betType().displayName();
    }

    protected abstract RouletteBetType<?> betType();

    @NotNull
    protected final MessageCreateBuilder betMessage(MessageEmbed... embed) {
        MessageCreateBuilder msg = new MessageCreateBuilder();
        msg.setEmbeds(embed);
        betActionRows().forEach(msg::addActionRow);
        return msg;
    }

    protected List<List<ItemComponent>> betActionRows() {
        List<List<ItemComponent>> buttons = new ArrayList<>(betButtons());
        buttons.add(betNavigationRow());
        return buttons;
    }

    protected abstract List<List<ItemComponent>> betButtons();

    protected List<ItemComponent> betNavigationRow() {
        return includeBackButton() ? List.of(CANCEL_BUTTON, BACK_BUTTON) : List.of(CANCEL_BUTTON);
    }

    protected boolean includeBackButton() {
        return false;
    }

    protected abstract void onDigitButton(RouletteSpace space);
}
