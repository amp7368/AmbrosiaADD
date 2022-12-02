package com.ambrosia.add.database.operation;

import com.ambrosia.add.discord.util.Emeralds;
import io.ebean.annotation.Aggregation;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table
public class OperationEntity {

    @Id
    public long id;

    @JoinColumn(nullable = false)
    public long clientId;

    @Column(nullable = false)
    public Timestamp dateCreated;
    @Column(nullable = false)
    public int changeAmount;
    @Column(nullable = false)
    public long conductorId;
    @Column(nullable = false)
    public OperationReason operationType;

    @Aggregation("sum(changeAmount)")
    public long sumAmount;

    public OperationEntity(long conductorId, long clientId, int changeAmount, OperationReason reason) {
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
