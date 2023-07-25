package com.ambrosia.roulette.game.player;

import com.ambrosia.add.discord.util.Emeralds;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class RoulettePlayerWinnings {

    private static final Comparator<RouletteBet> WIN_BET_COMPARATOR =
        Comparator.comparingInt(RouletteBet::winAmount).reversed()
            .thenComparing(bet -> bet.getType().getTypeId(), String.CASE_INSENSITIVE_ORDER);
    private static final Comparator<RouletteBet> LOSE_BET_COMPARATOR =
        Comparator.comparingInt(RouletteBet::getAmount).thenComparing(bet -> bet.getType().getTypeId(), String.CASE_INSENSITIVE_ORDER);

    private final RoulettePlayerGame player;
    private final List<RouletteBet> bets;
    private final List<RouletteBet> winningBets = new ArrayList<>();
    private final List<RouletteBet> losingBets = new ArrayList<>();
    private int winningsTotal;
    private int lossesTotal;

    public RoulettePlayerWinnings(RoulettePlayerGame player, int roll) {
        this.player = player;
        this.bets = player.getBets();

        this.winningsTotal = 0;
        this.lossesTotal = 0;
        for (RouletteBet bet : this.bets) {
            if (bet.isWinningSpace(roll)) {
                winningsTotal += bet.winAmount();
                winningBets.add(bet);
            } else {
                lossesTotal += bet.getAmount();
                losingBets.add(bet);
            }
        }

        this.winningBets.sort(WIN_BET_COMPARATOR);
        this.losingBets.sort(LOSE_BET_COMPARATOR);
    }

    private static String winBetToString(RouletteBet bet) {
        String betType = bet.getType().displayName();
        Field field = bet.toDiscordField(false);
        double multiplier = bet.getType().betMultiplier();
        String winAmount = Emeralds.message(bet.winAmount(), false);
        return "* **%s** (x%.0f) [+%s]\n * %s\n".formatted(betType, multiplier, winAmount, field.getValue());
    }

    private static String loseBetToString(RouletteBet bet) {
        String betType = bet.getType().displayName();
        Field field = bet.toDiscordField(false);
        String winAmount = Emeralds.message(bet.getAmount(), false);
        return "* **%s** [-%s]\n * %s\n".formatted(betType, winAmount, field.getValue());
    }

    public int getWinningsTotal() {
        return this.winningsTotal;
    }

    public List<RouletteBet> getWinningBets() {
        return this.winningBets;
    }

    public List<RouletteBet> getLosingBets() {
        return losingBets;
    }

    public Field shortSummaryField() {
        int change = getWinningsTotal() - getLossesTotal();
        String changePrefix;
        if (change == 0) changePrefix = "";
        else if (change > 0) changePrefix = "+";
        else changePrefix = "-";
        changePrefix = "%s ".formatted(changePrefix);

        String desc = "Win (%s) - Loss (%s) - Change (%s)".formatted(
            Emeralds.message(getWinningsTotal(), true),
            Emeralds.message(getLossesTotal(), true),
            changePrefix + Emeralds.message(Math.abs(change), true)
        );
        return new Field(player.getPlayerName(), desc, false);
    }

    private int getLossesTotal() {
        return this.lossesTotal;
    }

    public List<String> getWinningBetsDescriptions() {
        return getWinningBets().stream().map(RoulettePlayerWinnings::winBetToString).toList();
    }

    public List<String> getLosingBetsDescriptions() {
        return getLosingBets().stream().map(RoulettePlayerWinnings::loseBetToString).toList();
    }
}
