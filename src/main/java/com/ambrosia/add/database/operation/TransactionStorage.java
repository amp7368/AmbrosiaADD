package com.ambrosia.add.database.operation;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.database.operation.query.QTransactionEntity;
import io.ebean.DB;
import io.ebean.Transaction;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class TransactionStorage {

    private static TransactionStorage instance;

    public TransactionStorage() {
        instance = this;
    }

    public static TransactionStorage get() {
        return instance;
    }

    public TransactionEntity createOperation(long conductorId, long client, long amount, TransactionType transactionType) {
        try (Transaction transaction = DB.getDefault().beginTransaction()) {
            TransactionEntity operation = new TransactionEntity(conductorId, client, amount, transactionType);
            ClientEntity clientEntity = ClientStorage.get().findByUUID(client);
            if (clientEntity == null) throw new IllegalStateException("Client " + client + " does not exist!");
            clientEntity.addCredits(transactionType, amount);
            DB.getDefault().save(operation, transaction);
            DB.getDefault().update(clientEntity, transaction);
            transaction.commit();
            return operation;
        }
    }

    public TransactionEntity delete(Long id) {
        TransactionEntity operation = DB.getDefault().find(TransactionEntity.class, id);
        if (operation == null) return null;
        operation.delete();
        return operation;
    }

    public List<TransactionEntity> findTransactions(long clientId) {
        return new QTransactionEntity().where().clientId.eq(clientId).findList();
    }

    @NotNull
    public List<TransactionEntity> aggregateByType(ClientEntity client) {
        QTransactionEntity o = QTransactionEntity.alias();
        return new QTransactionEntity().select(o.operationType, o.sumAmount).clientId.eq(client.uuid).findList();
    }
}
