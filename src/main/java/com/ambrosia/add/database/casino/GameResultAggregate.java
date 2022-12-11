package com.ambrosia.add.database.casino;

import io.ebean.annotation.Aggregation;
import io.ebean.annotation.Sum;
import io.ebean.annotation.View;
import javax.persistence.Entity;

@Entity
@View(name = "game_result_entity")
public class GameResultAggregate {


    public String name;
    public String conclusion;

    @Aggregation("count(conclusion)")
    public Long count;

    @Sum
    public Long deltaWinnings;
}
