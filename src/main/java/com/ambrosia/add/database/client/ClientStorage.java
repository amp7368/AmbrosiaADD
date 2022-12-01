package com.ambrosia.add.database.client;

import com.ambrosia.add.database.client.query.QClientEntity;
import io.ebean.DB;
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
        boolean previousExists = this.find(clientName) != null;
        if (previousExists) return null;
        ClientEntity client = new ClientEntity(conductorId, clientName);
        DB.getDefault().save(client);
        return client;
    }
}
