package com.ambrosia.add.database.operation;

import com.ambrosia.add.discord.util.Emeralds;
import io.ebean.Model;
import io.ebean.annotation.Aggregation;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table
public class TransactionEntity extends Model {

    @Id
    public long id;

    @JoinColumn(nullable = false)
    public long clientId;

    @Column(nullable = false)
    public Timestamp dateCreated;
    @Column(nullable = false)
    public long changeAmount;
    @Column(nullable = false)
    public long conductorId;
    @Column(nullable = false)
    public TransactionType operationType;

    @Aggregation("sum(changeAmount)")
    public long sumAmount;

    public TransactionEntity(long conductorId, long clientId, long changeAmount, TransactionType reason) {
        this.conductorId = conductorId;
        this.clientId = clientId;
        this.changeAmount = changeAmount;
        this.dateCreated = new Timestamp(System.currentTimeMillis());
        this.operationType = reason;
    }

    public String display() {
        return String.format("(%s) %s", Emeralds.message(changeAmount, Integer.MAX_VALUE, false), operationType.displayName());
    }

}
