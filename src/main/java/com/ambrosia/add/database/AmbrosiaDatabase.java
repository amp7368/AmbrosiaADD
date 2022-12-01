package com.ambrosia.add.database;

import apple.lib.modules.AppleModule;
import apple.lib.modules.configs.factory.AppleConfigLike;
import com.ambrosia.add.Ambrosia;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.database.operation.OperationEntity;
import com.ambrosia.add.database.operation.OperationStorage;
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
        new OperationStorage();
    }

    @NotNull
    private static DatabaseConfig configureDatabase(DataSourceConfig dataSourceConfig) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setDataSourceConfig(dataSourceConfig);
        dbConfig.setDdlGenerate(true);
        dbConfig.setDdlRun(false);
        dbConfig.addAll(List.of(OperationEntity.class, ClientEntity.class));
        return dbConfig;
    }

    @NotNull
    private static DataSourceConfig configureDataSource(AmbrosiaDatabaseConfig loadedConfig) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUsername(loadedConfig.getUsername());
        dataSourceConfig.setPassword(loadedConfig.getPassword());
        dataSourceConfig.setUrl(loadedConfig.getUrl());
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
