package com.ambrosia.add.database.client;

import com.ambrosia.add.database.client.query.QClientEntity;
import io.ebean.DB;
import java.util.Optional;

public class ClientStorage {

    private static ClientStorage instance;

    public ClientStorage() {
        instance = this;
    }

    public static ClientStorage get() {
        return instance;
    }

    public void save(ClientEntity client) {
        DB.getDefault().save(client);
    }

    public Optional<ClientEntity> find(String clientName) {
        return new QClientEntity().where().displayName.eq(clientName).findOneOrEmpty();
    }
}
