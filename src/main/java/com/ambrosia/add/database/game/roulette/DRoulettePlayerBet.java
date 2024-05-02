package com.ambrosia.add.database.game.roulette;

import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import io.ebean.Model;
import io.ebean.annotation.DbJson;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "roulette_player_bet")
public class DRoulettePlayerBet extends Model {

    @Id
    private long id;
    @ManyToOne(optional = false)
    private DRoulettePlayerGame player;
    @Column(nullable = false)
    private final boolean isWin;
    @Column(nullable = false)
    private final int originalBet;
    @Column(nullable = false)
    private final int deltaAmount;
    @DbJson
    private final String betDetails;
    @Column(nullable = false)
    private final String type;

    public DRoulettePlayerBet(DRoulettePlayerGame player, RouletteBet bet, boolean isWin) {
        this.player = player;
        this.isWin = isWin;
        this.betDetails = bet.toJson();
        this.type = bet.getType().getTypeName();
        this.originalBet = bet.getAmount();
        this.deltaAmount = isWin ? bet.winAmount() : -bet.getAmount();
    }

    public RouletteBetType<?> getType() {
        return RouletteBetType.fromName(this.type);
    }
}
