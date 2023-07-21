package com.ambrosia.roulette.game.game;

import com.ambrosia.add.api.CreditReservation;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.player.RoulettePlayerGame;
import com.ambrosia.roulette.game.table.gui.RouletteTableBettingPage;
import com.ambrosia.roulette.game.table.gui.RouletteTableGui;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class RouletteGame {

    private final TextChannel channel;
    private final Map<Long, RoulettePlayerGame> players = new HashMap<>();
    private RouletteTableGui tableGui;

    public RouletteGame(TextChannel channel) {
        this.channel = channel;
    }

    public void start(RouletteTableGui tableGui) {
        this.tableGui = tableGui;
        this.tableGui.addPage(new RouletteTableBettingPage(tableGui));
        this.tableGui.send();
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
            long clientId = reservation.getClient().uuid;
            RoulettePlayerGame player = players.get(clientId);
            if (player != null) return player;

            // create player
            player = new RoulettePlayerGame(this, reservation, discord);
            players.put(clientId, player);
            return player;
        }
    }

    public void updateBetsUI() {
        this.tableGui.updateBetsUI();
    }

    public List<RouletteBet> getLatestBets() {
        return this.players.values().stream()
            .flatMap(p -> p.getBets().stream())
            .sorted(Comparator.comparing(RouletteBet::getTimestamp).reversed())
            .toList();
    }
}
