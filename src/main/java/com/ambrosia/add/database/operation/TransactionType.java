package com.ambrosia.add.database.operation;

import apple.utilities.util.Pretty;
import io.ebean.annotation.DbEnumValue;

public enum TransactionType {
    WITHDRAW(0),
    DEPOSIT(1),
    WIN(2),
    LOSS(3);

    private final int id;

    TransactionType(int id) {
        this.id = id;
    }

    @DbEnumValue
    public int getId() {
        return id;
    }

    public String displayName() {
        return Pretty.upperCaseFirst(this.name());
    }
}
