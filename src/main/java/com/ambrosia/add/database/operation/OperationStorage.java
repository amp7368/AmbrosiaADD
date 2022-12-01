package com.ambrosia.add.database.operation;

import com.ambrosia.add.database.client.ClientEntity;
import io.ebean.DB;
import io.ebean.Transaction;

public class OperationStorage {

    private static OperationStorage instance;

    public OperationStorage() {
        instance = this;
    }

    public static OperationStorage get() {
        return instance;
    }

    public ClientEntity saveOperation(long conductorId, ClientEntity client, int amount, OperationReason operationReason) {
        try (Transaction transaction = DB.getDefault().beginTransaction()) {
            OperationEntity operation = new OperationEntity(conductorId, client.uuid, amount, operationReason);
            client.credits += amount;
            DB.getDefault().save(operation, transaction);
            DB.getDefault().save(client, transaction);
            return client;
        }
    }
}
