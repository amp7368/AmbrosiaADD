package com.ambrosia.add.database.game;

import io.ebean.Model;
import io.ebean.annotation.Identity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
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
    public long transactionId;


    public GameResultEntity(String name) {
        this.name = name;
    }

    public void addWinnings(long winnings) {
        this.deltaWinnings += winnings;
    }

}
