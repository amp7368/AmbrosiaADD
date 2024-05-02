package com.ambrosia.roulette.game.table;

import com.ambrosia.add.api.CreditReservation;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.game.roulette.DRouletteTableGame;
import com.ambrosia.add.database.game.roulette.RouletteGameApi;
import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.player.RoulettePlayerGame;
import com.ambrosia.roulette.game.table.gui.RouletteTableBettingPage;
import com.ambrosia.roulette.game.table.gui.RouletteTableGui;
import com.ambrosia.roulette.table.RouletteSpace;
import discord.util.dcf.gui.base.GuiReplyFirstMessage;
import java.security.SecureRandom;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.Nullable;

public class RouletteGame {

    private final TextChannel channel;
    private final Map<Long, RoulettePlayerGame> players = new HashMap<>();
    private final SecureRandom random = new SecureRandom();
    private final int id;
    private RouletteTableGui gui;
    private RouletteSpace spinResult;
    private boolean isWinningsAwarded = false;

    public RouletteGame(int id, TextChannel channel) {
        this.id = id;
        this.channel = channel;
    }

    public void start(RouletteTableGui tableGui) {
        this.gui = tableGui;
        this.gui.addPage(new RouletteTableBettingPage(tableGui));
        this.gui.send();
    }

    public List<RoulettePlayerGame> getPlayers() {
        synchronized (players) {
            return players.values().stream()
                .sorted(RoulettePlayerGame.ROULETTE_PLAYER_COMPARATOR)
                .toList();
        }
    }

    public RoulettePlayerGame getOrCreatePlayer(Member discord, CreditReservation reservation) {
        synchronized (players) {
            long clientId = reservation.getClient().id;
            RoulettePlayerGame player = players.get(clientId);
            if (player != null) return player;

            // create player
            player = new RoulettePlayerGame(this, reservation, discord);
            players.put(clientId, player);
            return player;
        }
    }

    public void updateBetsUI() {
        this.gui.updateBetsUI();
    }

    public List<RouletteBet> getLatestBets() {
        return this.players.values().stream()
            .flatMap(p -> p.getBets().stream())
            .sorted(Comparator.comparing(RouletteBet::getTimestamp).reversed())
            .toList();
    }

    public RouletteSpace spin() {
        int roll = this.random.nextInt(Roulette.TABLE.spaces(true).size());
        return this.spinResult = Roulette.TABLE.getSpace(roll);
    }

    public RouletteSpace getSpinResult() {
        return this.spinResult;
    }

    public void awardWinnings() {
        synchronized (this) {
            if (this.isWinningsAwarded) return;
            this.isWinningsAwarded = true;
        }
        RouletteGameManager.removeChannelGame(this.channel);

        DRouletteTableGame dbTableGame = RouletteGameApi.createTable(this.spinResult);
        for (RoulettePlayerGame player : getPlayers()) {
            player.awardWinnings(dbTableGame, this.spinResult.digit());
        }
    }

    public int getId() {
        return id;
    }

    @Nullable
    public RoulettePlayerGame getPlayer(ClientEntity user) {
        return this.players.get(user.id);
    }

    public void resendGui(GuiReplyFirstMessage reply) {
        this.gui = this.gui.recreate(reply);
        this.gui.send();
    }

    public void resetLastBetTimer() {
        this.gui.resetLastBetTimer();
    }
}
