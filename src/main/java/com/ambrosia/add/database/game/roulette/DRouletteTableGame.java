package com.ambrosia.add.database.game.roulette;

import com.ambrosia.roulette.table.RouletteSpace;
import io.ebean.Model;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roulette_table_game")
public class DRouletteTableGame extends Model {

    @Id
    private long id;

    @Column(nullable = false)
    private int spinResult;
    @Column(nullable = false)
    private Timestamp playedAt;
    @OneToMany
    private final List<DRoulettePlayerGame> players = new ArrayList<>();

    public DRouletteTableGame(RouletteSpace spinResult) {
        this.spinResult = spinResult.digit();
        this.playedAt = Timestamp.from(Instant.now());
    }

    public void addPlayer(DRoulettePlayerGame player) {
        this.players.add(player);
    }
}
