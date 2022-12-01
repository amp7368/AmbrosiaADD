package com.ambrosia.add.database.operation;

import io.ebean.annotation.DbEnumValue;

public enum OperationReason {
    WITHDRAW(0),
    DEPOSIT(1),
    WIN(2),
    LOSS(3);

    private final int id;

    OperationReason(int id) {
        this.id = id;
    }

    @DbEnumValue
    public int getId() {
        return id;
    }
}
