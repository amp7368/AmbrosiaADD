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

    public TransactionEntity createOperation(long conductorId, long client, int amount, TransactionType transactionType)
        throws CreateTransactionException {
        try (Transaction transaction = DB.getDefault().beginTransaction()) {
            TransactionEntity operation = new TransactionEntity(conductorId, client, amount, transactionType);
            ClientEntity clientEntity = ClientStorage.get().findByUUID(client);
            if (clientEntity == null) throw new IllegalStateException("Client " + client + " does not exist!");
            if (clientEntity.credits - amount < 0) {
                throw new CreateTransactionException("Not enough credits");
            }
            clientEntity.addCredits(transactionType, amount);
            DB.getDefault().save(operation, transaction);
            DB.getDefault().update(clientEntity, transaction);
            transaction.commit();
            return operation;
        }
    }

    public ClientEntity trade(long clientTradingUUID, long clientReceivingUUID, Integer amount) {
        try (Transaction transaction = DB.getDefault().beginTransaction()) {
            // create the transactions
            TransactionEntity tradeGive = new TransactionEntity(clientTradingUUID, clientTradingUUID, -amount,
                TransactionType.TRADE_GIVE);
            TransactionEntity tradeGet = new TransactionEntity(clientTradingUUID, clientReceivingUUID, amount,
                TransactionType.TRADE_GET);

            // get the clients (in this transaction
            ClientEntity clientTrading = ClientStorage.get().findByUUID(clientTradingUUID);
            ClientEntity clientReceiving = ClientStorage.get().findByUUID(clientReceivingUUID);
            if (clientReceiving == null || clientTrading == null)
                throw new IllegalStateException("Client " + clientTradingUUID + " | " + clientReceivingUUID + " does not exist!");
            if (clientTrading.credits < amount) {
                return null;
            }
            // update the DB
            clientTrading.addCredits(TransactionType.TRADE_GIVE, -amount);
            clientReceiving.addCredits(TransactionType.TRADE_GET, amount);
            DB.getDefault().save(tradeGive, transaction);
            DB.getDefault().update(clientTrading, transaction);
            DB.getDefault().save(tradeGet, transaction);
            DB.getDefault().update(clientReceiving, transaction);
            transaction.commit();
            return clientTrading;
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
