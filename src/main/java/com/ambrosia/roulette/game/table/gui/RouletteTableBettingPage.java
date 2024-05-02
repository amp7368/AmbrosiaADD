package com.ambrosia.roulette.game.table.gui;

import static com.ambrosia.roulette.img.RouletteImage.IMAGE_BETTING_TABLE;

import com.ambrosia.add.discord.util.Emeralds;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.player.RoulettePlayerGame;
import com.ambrosia.roulette.game.table.RouletteGame;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteTableBettingPage extends DCFGuiPage<RouletteTableGui> {

    private static final Button SPIN_BUTTON = Button.primary("spin", "SPIN!!!");

    public RouletteTableBettingPage(RouletteTableGui tableGui) {
        super(tableGui);
        this.initButtons();
    }

    private void initButtons() {
        registerButton(SPIN_BUTTON.getId(), this::spin);
    }

    private void spin(ButtonInteractionEvent event) {
        if (this.isDisableSpin()) return;
        getParent().addSubPage(new RouletteSpinWarmup(getParent()));
        new Thread(() -> {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }
            RouletteSpinningPage spinPage = new RouletteSpinningPage(getParent(), getGame().spin());
            getParent().addSubPage(spinPage);
            editMessage();
            long start = System.currentTimeMillis();
            getGame().awardWinnings();
            try {
                long duration = System.currentTimeMillis() - start;
                Thread.sleep(13500 - duration);
            } catch (InterruptedException ignored) {
            }
            getParent().addSubPage(new RouletteSpinWinningsPage(getParent(), spinPage.getSpinImage().last()));
            editMessage();
        }).start();
    }

    private RouletteGame getGame() {
        return getParent().getGame();
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Betting table");
        embed.setImage(IMAGE_BETTING_TABLE);
        for (RoulettePlayerGame player : parent.getGame().getPlayers()) {
            String playerTotal = Emeralds.message(player.getBetTotal(), true);
            String status = player.isBettingDisplay();
            String msg = "Total (%s) === Status **%s**".formatted(playerTotal, status);
            embed.addField(player.getPlayerName(), msg, false);
        }
        List<RouletteBet> bets = getParent().getGame().getLatestBets();
        StringBuilder description = new StringBuilder();
        for (int i = 0, max = Math.min(bets.size(), 20); i < max; i++) {
            RouletteBet bet = bets.get(i);
            Field betField = bet.toDiscordField();

            String playerName = bet.getPlayer().getPlayerName();
            String line = "***%s*** (%s) --- __%s__\n".formatted(
                betField.getName(),
                betField.getValue(),
                playerName);
            if (MessageEmbed.DESCRIPTION_MAX_LENGTH < description.length() + line.length())
                break;
            description.append(line);
        }
        embed.setDescription(description);
        long untilBettingStale = getParent().getUntilBettingStaleSeconds();
        embed.setFooter("%ds until betting is stale".formatted(untilBettingStale));

        ActionRow actions = ActionRow.of(SPIN_BUTTON.withDisabled(isDisableSpin()));
        return buildCreate(embed.build(), actions);
    }

    private boolean isDisableSpin() {
        boolean isBettingStale = !parent.getUntilBettingStale().isNegative();
        return isBettingStale && isAnyPlayerBetting();
    }

    private boolean isAnyPlayerBetting() {
        List<RoulettePlayerGame> players = this.getParent().getGame().getPlayers();
        if (players.isEmpty()) return true;
        return players.stream().anyMatch(RoulettePlayerGame::isBetting);
    }
}
