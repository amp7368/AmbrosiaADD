package com.ambrosia.add.database;

import apple.lib.ebean.database.AppleEbeanDatabaseMetaConfig;
import apple.lib.modules.AppleModule;
import apple.lib.modules.configs.factory.AppleConfigLike;
import com.ambrosia.add.Ambrosia;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.database.game.GameStorage;
import com.ambrosia.add.database.operation.TransactionStorage;
import java.util.List;

public class AmbrosiaDatabaseModule extends AppleModule {

    @Override
    public void onLoad() {
        AppleEbeanDatabaseMetaConfig.configureMeta(
            Ambrosia.class,
            Ambrosia.get().getDataFolder(),
            logger()::error,
            logger()::info);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new AmbrosiaDatabase();

        new ClientStorage();
        new TransactionStorage();
        new GameStorage();

    }

    @Override
    public List<AppleConfigLike> getConfigs() {
        return List.of(configJson(AmbrosiaDatabaseConfig.class, "Database.config", "Config"));
    }

    @Override
    public String getName() {
        return "Database";
    }
}
