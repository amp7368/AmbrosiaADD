package com.ambrosia.add.database.game;

import com.ambrosia.add.database.operation.TransactionEntity;
import io.ebean.Model;
import io.ebean.annotation.Identity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "game_result_entity")
public class GameResultEntity extends Model {

    @Id
    @Identity
    public Long id;


    public String name;

    @Column(columnDefinition = "JSON")
    public String extraResults;

    public String conclusion;
    public int deltaWinnings = 0;
    public int originalBet;

    @OneToOne(targetEntity = TransactionEntity.class)
    public TransactionEntity transaction;


    public GameResultEntity(String name) {
        this.name = name;
    }
}
