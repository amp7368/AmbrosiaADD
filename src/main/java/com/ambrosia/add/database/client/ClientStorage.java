package com.ambrosia.add.database.client;

import com.ambrosia.add.database.client.query.QClientEntity;
import com.ambrosia.add.database.operation.OperationEntity;
import com.ambrosia.add.database.operation.query.QOperationEntity;
import com.google.gson.Gson;
import io.ebean.DB;
import io.ebean.Transaction;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class ClientStorage {

    private static ClientStorage instance;

    public ClientStorage() {
        instance = this;
    }

    public static ClientStorage get() {
        return instance;
    }

    public ClientEntity find(String clientName) {

        ClientEntity client = new QClientEntity().where().displayName.ieq(clientName).findOne();
        if (client == null) return null;
        QOperationEntity o = QOperationEntity.alias();
        List<OperationEntity> byOperationType = new QOperationEntity().select(o.operationType, o.sumAmount).clientId.eq(client.uuid)
            .findList();

        for (OperationEntity aggregation : byOperationType) {
            client.totals.put(aggregation.operationType, aggregation.sumAmount);
        }
        return client;

    }

    @Nullable
    public ClientEntity saveClient(long conductorId, String clientName) {
        ClientEntity client = new ClientEntity(conductorId, clientName);
        try (Transaction transaction = DB.getDefault().beginTransaction()) {
            boolean isUnique = DB.getDefault().checkUniqueness(client, transaction).isEmpty();
            if (!isUnique) return null;
            client.save(transaction);
            transaction.commit();
        }
        return client;
    }
}
