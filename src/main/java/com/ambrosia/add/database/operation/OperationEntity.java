package com.ambrosia.add.database.operation;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    public Date dateCreated;
    @Column(nullable = false)
    public int changeAmount;
    @Column(nullable = false)
    public long conductorId;

    @Enumerated(EnumType.STRING)
    public OperationReason operationType;

    public OperationEntity(long conductorId, long clientId, int changeAmount, OperationReason reason) {
        this.conductorId = conductorId;
        this.clientId = clientId;
        this.changeAmount = changeAmount;
        this.dateCreated = new Date(System.currentTimeMillis());
    }

    public OperationEntity() {
    }

}
