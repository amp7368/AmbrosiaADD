package com.ambrosia.add.database;

import apple.lib.ebean.database.AppleEbeanDatabase;
import apple.lib.ebean.database.config.AppleEbeanDatabaseConfig;
import com.ambrosia.add.database.casino.GameResultAggregate;
import com.ambrosia.add.database.client.ClientDiscordDetails;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientMinecraftDetails;
import com.ambrosia.add.database.game.GameResultEntity;
import com.ambrosia.add.database.game.roulette.DRoulettePlayerBet;
import com.ambrosia.add.database.game.roulette.DRoulettePlayerGame;
import com.ambrosia.add.database.game.roulette.DRouletteTableGame;
import com.ambrosia.add.database.operation.TransactionEntity;
import java.util.Collection;
import java.util.List;

public class AmbrosiaDatabase extends AppleEbeanDatabase {

    @Override
    protected void addEntities(List<Class<?>> entities) {
        // embedded
        entities.add(ClientEntity.class);
        entities.addAll(List.of(ClientMinecraftDetails.class, ClientDiscordDetails.class));

        entities.addAll(List.of(TransactionEntity.class, GameResultEntity.class));
        entities.addAll(List.of(DRoulettePlayerBet.class, DRoulettePlayerGame.class, DRouletteTableGame.class));
        // aggregates
        entities.add(GameResultAggregate.class);
    }

    @Override
    protected Collection<Class<?>> getQueryBeans() {
        return List.of();
    }

    @Override
    protected AppleEbeanDatabaseConfig getConfig() {
        return AmbrosiaDatabaseConfig.get();
    }

    @Override
    protected boolean isDefault() {
        return true;
    }

    @Override
    protected String getName() {
        return "Ambrosia";
    }
}
