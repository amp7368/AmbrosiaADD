package com.ambrosia.add.database.game.roulette;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.game.GameResultEntity;
import com.ambrosia.roulette.game.player.RoulettePlayerGame;
import io.ebean.Model;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "roulette_player_game")
public class DRoulettePlayerGame extends Model {

    @Id
    private long id;
    @ManyToOne(optional = false)
    private DRouletteTableGame rouletteTable;
    @ManyToOne(optional = false)
    private ClientEntity client;
    @OneToOne(optional = false)
    private GameResultEntity gameResult;
    @ManyToOne
    private final List<DRoulettePlayerBet> bets = new ArrayList<>();

    public DRoulettePlayerGame(DRouletteTableGame dbTableGame, RoulettePlayerGame playerGame, GameResultEntity gameResult) {
        this.rouletteTable = dbTableGame;
        this.gameResult = gameResult;
        this.client = playerGame.getPlayer();
    }

    public DRoulettePlayerGame addBet(DRoulettePlayerBet bet) {
        this.bets.add(bet);
        return this;
    }

    public List<DRoulettePlayerBet> getBets() {
        return this.bets;
    }
}
