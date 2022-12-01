package com.ambrosia.add.database.client;

import com.ambrosia.add.database.client.query.QClientEntity;
import io.ebean.DB;
import io.ebean.Transaction;
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
        return new QClientEntity().where().displayName.ieq(clientName).findOne();
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
