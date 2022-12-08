package com.ambrosia.add.database.client;

import com.ambrosia.add.database.client.query.QClientEntity;
import com.ambrosia.add.database.operation.TransactionEntity;
import com.ambrosia.add.database.operation.TransactionStorage;
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

    public ClientEntity findByName(String clientName) {
        ClientEntity client = new QClientEntity().where().displayName.ieq(clientName).findOne();
        if (client == null) return null;
        List<TransactionEntity> byOperationType = TransactionStorage.get().aggregateByType(client);

        for (TransactionEntity aggregation : byOperationType) {
            client.totals.put(aggregation.operationType, aggregation.sumAmount);
        }
        return client;
    }

    public ClientEntity findByDiscord(long discordId) {
        return new QClientEntity().where().discord.discordId.eq(discordId).findOne();
    }

    @Nullable
    public ClientEntity createClient(long conductorId, String clientName) {
        ClientEntity client = new ClientEntity(conductorId, clientName);
        client.setMinecraft(ClientMinecraftDetails.fromUsername(clientName));
        try (Transaction transaction = DB.getDefault().beginTransaction()) {
            boolean isUnique = DB.getDefault().checkUniqueness(client, transaction).isEmpty();
            if (!isUnique) return null;
            client.save(transaction);
            transaction.commit();
        }
        return client;
    }

    @Nullable
    public ClientEntity findByUUID(long uuid) {
        return new QClientEntity().where().uuid.eq(uuid).findOne();

    }
}
