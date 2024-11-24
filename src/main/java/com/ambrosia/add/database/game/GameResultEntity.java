package com.ambrosia.add.database.game;

import com.ambrosia.add.database.operation.TransactionEntity;
import io.ebean.Model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "game_result_entity")
public class GameResultEntity extends Model {

    @Id
    public Long id;

    @Column
    public String name;

    @Column(columnDefinition = "JSON")
    public String extraResults;
    @Column
    public String conclusion;
    @Column
    public int deltaWinnings = 0;
    @Column
    public int originalBet;

    @OneToOne
    public TransactionEntity transaction;


    public GameResultEntity(String name) {
        this.name = name;
    }
}
