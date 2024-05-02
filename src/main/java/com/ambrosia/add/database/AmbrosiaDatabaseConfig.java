package com.ambrosia.add.database;

import apple.lib.ebean.database.config.AppleEbeanMysqlConfig;

public class AmbrosiaDatabaseConfig extends AppleEbeanMysqlConfig {

    private static AmbrosiaDatabaseConfig instance;

    public AmbrosiaDatabaseConfig() {
        instance = this;
    }

    public static AmbrosiaDatabaseConfig get() {
        return instance;
    }
}
