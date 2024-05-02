package com.ambrosia.roulette.game.player;

import com.ambrosia.add.api.CreditReservation;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.game.GameBase;
import com.ambrosia.add.database.game.roulette.DRouletteTableGame;
import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.player.gui.RoulettePlayerGui;
import com.ambrosia.roulette.game.table.RouletteGame;
import discord.util.dcf.gui.base.GuiReplyFirstMessage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

public class RoulettePlayerGame extends GameBase {

    public static final Comparator<RoulettePlayerGame> ROULETTE_PLAYER_COMPARATOR =
        Comparator.comparing(RoulettePlayerGame::getPlayerName, String.CASE_INSENSITIVE_ORDER);
    private final RouletteGame tableGame;
    private final Member discord;
    protected String name;
    protected List<RouletteBet> bets = new ArrayList<>();
    protected RoulettePlayerWinnings winnings;
    private boolean isBetting = true;
    private RoulettePlayerGui gui;
    private RoulettePartialBet partialBet;
    private int betTotal = 0;

    public RoulettePlayerGame(RouletteGame tableGame, CreditReservation reservation, Member discord) {
        super(reservation);
        this.tableGame = tableGame;
        this.discord = discord;
    }

    public String getPlayerName() {
        return getPlayer().displayName;
    }

    public ClientEntity getPlayer() {
        return creditReservation.getClient();
    }

    @Override
    public String getName() {
        return Roulette.GAME_NAME;
    }

    public boolean startBet(int reserved) {
        if (this.partialBet != null) return false;
        this.setIsBetting(true);
        this.partialBet = new RoulettePartialBet(reserved, this);
        return true;
    }

    public void resendGui(GuiReplyFirstMessage reply) {
        this.gui = this.gui.recreate(reply);
        this.gui.send();
    }

    public void setGui(RoulettePlayerGui gui) {
        this.gui = gui;
    }

    public RoulettePartialBet getPartialBet() {
        return this.partialBet;
    }

    public List<RouletteBet> getBets() {
        return this.bets;
    }

    public synchronized RouletteBet finishBet(Function<RoulettePartialBet, RouletteBet> finalizeBet) {
        RouletteBet bet = finalizeBet.apply(partialBet);
        this.bets.add(bet);
        this.betTotal += bet.getAmount();
        this.partialBet = null;
        this.tableGame.resetLastBetTimer();
        this.tableGame.updateBetsUI();
        return bet;
    }

    public void addBetsFieldSummary(EmbedBuilder embed) {
        getBets().stream()
            .map(RouletteBet::toDiscordField)
            .forEach(embed::addField);
    }

    public Member getDiscord() {
        return this.discord;
    }

    public void setIsBetting(boolean isBetting) {
        boolean shouldUpdate = this.isBetting != isBetting;
        this.isBetting = isBetting;
        if (!this.isBetting) this.partialBet = null;
        if (shouldUpdate) this.tableGame.updateBetsUI();
    }

    public String isBettingDisplay() {
        return this.isBetting() ? "Betting" : "Waiting";
    }

    public boolean isBetting() {
        return this.isBetting;
    }

    public int getBetTotal() {
        return this.betTotal;
    }

    public void awardWinnings(DRouletteTableGame dbTableGame, int roll) {
        this.winnings = new RoulettePlayerWinnings(this, roll);

        RouletteEndGame.endGame(this, dbTableGame, creditReservation);

    }

    public RoulettePlayerWinnings getWinnings() {
        return this.winnings;
    }
}
