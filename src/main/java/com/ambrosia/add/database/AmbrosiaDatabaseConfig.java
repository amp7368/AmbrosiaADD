package com.ambrosia.add.database;

public class AmbrosiaDatabaseConfig {

    private static AmbrosiaDatabaseConfig instance;

    public AmbrosiaDatabaseConfig() {
        instance = this;
    }

    public String username = "${username}";
    public String password = "${password}";
    public String host = "${host}";
    public String port = "${port}";
    public String database = "add";

    public static AmbrosiaDatabaseConfig get() {
        return instance;
    }

    public String getUrl() {
        return "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database;
    }

    public String getDatabase() {
        return this.database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
