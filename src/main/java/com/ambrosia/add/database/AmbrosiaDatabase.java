package com.ambrosia.add.database;

import apple.lib.modules.AppleModule;
import apple.lib.modules.configs.factory.AppleConfigLike;
import com.ambrosia.add.Ambrosia;
import com.ambrosia.add.database.client.ClientDiscordDetails;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientMinecraftDetails;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.database.game.GameResultEntity;
import com.ambrosia.add.database.game.GameStorage;
import com.ambrosia.add.database.operation.TransactionEntity;
import com.ambrosia.add.database.operation.TransactionStorage;
import io.ebean.DatabaseFactory;
import io.ebean.annotation.Platform;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.dbmigration.DbMigration;
import java.io.IOException;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AmbrosiaDatabase extends AppleModule {

    public static void main(String[] args) throws IOException {
        // doesn't work
        DbMigration migration = DbMigration.create();
        migration.setPlatform(Platform.MYSQL);
        migration.generateMigration();
    }

    @Override
    public void onLoad() {
        DataSourceConfig dataSourceConfig = configureDataSource(AmbrosiaDatabaseConfig.get());
        DatabaseConfig dbConfig = configureDatabase(dataSourceConfig);
        DatabaseFactory.createWithContextClassLoader(dbConfig, Ambrosia.class.getClassLoader());
        logger().info("Successfully created database");
        new ClientStorage();
        new TransactionStorage();
        new GameStorage();
    }

    @NotNull
    private static DatabaseConfig configureDatabase(DataSourceConfig dataSourceConfig) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setDataSourceConfig(dataSourceConfig);
        dbConfig.setDdlGenerate(true);
//        dbConfig.setDdlRun(true);
        dbConfig.addAll(
            List.of(TransactionEntity.class, GameResultEntity.class, ClientEntity.class, ClientMinecraftDetails.class,
                ClientDiscordDetails.class));
        return dbConfig;
    }

    @NotNull
    private static DataSourceConfig configureDataSource(AmbrosiaDatabaseConfig loadedConfig) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUsername(loadedConfig.getUsername());
        dataSourceConfig.setPassword(loadedConfig.getPassword());
        dataSourceConfig.setUrl(loadedConfig.getUrl());
        dataSourceConfig.setAutoCommit(true);
        return dataSourceConfig;
    }

    @Override
    public List<AppleConfigLike> getConfigs() {
        return List.of(configJson(AmbrosiaDatabaseConfig.class, "Database.config"));
    }

    @Override
    public String getName() {
        return "Database";
    }
}
