package com.ambrosia.add.database.operation;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.operation.query.QOperationEntity;
import io.ebean.DB;
import io.ebean.Transaction;
import java.util.List;

public class OperationStorage {

    private static OperationStorage instance;

    public OperationStorage() {
        instance = this;
    }

    public static OperationStorage get() {
        return instance;
    }

    public OperationEntity saveOperation(long conductorId, ClientEntity client, int amount, OperationReason operationReason) {
        try (Transaction transaction = DB.getDefault().beginTransaction()) {
            OperationEntity operation = new OperationEntity(conductorId, client.uuid, amount, operationReason);
            client.addCredits(operationReason, amount);
            DB.getDefault().save(operation, transaction);
            DB.getDefault().update(client, transaction);
            transaction.commit();
            return operation;
        }
    }

    public OperationEntity delete(Long id) {
        OperationEntity operation = DB.getDefault().find(OperationEntity.class, id);
        int delete = DB.getDefault().delete(OperationEntity.class, id);
        return operation;
    }

    public List<OperationEntity> findTransactions(long clientId) {
        return new QOperationEntity().where().clientId.eq(clientId).findList();
    }
}
