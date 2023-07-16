package com.ambrosia.roulette.game.player;

import com.ambrosia.add.api.CreditReservation;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.game.GameBase;
import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.RouletteBet;
import com.ambrosia.roulette.game.game.RouletteGame;
import com.ambrosia.roulette.game.player.gui.RoulettePlayerGui;
import discord.util.dcf.gui.base.GuiReplyFirstMessage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RoulettePlayerGame extends GameBase {

    public static final Comparator<RoulettePlayerGame> ROULETTE_PLAYER_COMPARATOR =
        Comparator.comparing(RoulettePlayerGame::getPlayerName, String.CASE_INSENSITIVE_ORDER);
    private final RoulettePlayerBetStatus status;
    private final RouletteGame tableGame;
    protected String name;
    protected List<RouletteBet> bets = new ArrayList<>();
    private RoulettePlayerGui gui;
    private RoulettePartialBet partialBet;

    public RoulettePlayerGame(RouletteGame tableGame, CreditReservation reservation) {
        super(reservation);
        this.tableGame = tableGame;
        this.status = RoulettePlayerBetStatus.WAITING;
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

    public RoulettePlayerBetStatus getStatus() {
        return status;
    }

    public boolean startBet(int reserved) {
        if (this.partialBet != null) return false;
        this.partialBet = new RoulettePartialBet(reserved);
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
}
