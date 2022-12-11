package com.ambrosia.add.api;

import apple.lib.modules.AppleModule;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.database.game.GameBase;
import com.ambrosia.add.database.game.GameStorage;

public class AmbrosiaAPI extends AppleModule {

    private static AmbrosiaAPI instance;

    public static AmbrosiaAPI get() {
        return instance;
    }

    public AmbrosiaAPI() {
        instance = this;
    }

    public synchronized CreditReservation reserve(long discordId, long reserve) {
        ClientEntity client = ClientStorage.get().findByDiscord(discordId);
        if (client == null) return new CreditReservation(null, reserve).setRejected(CreditReservationRejection.NO_CLIENT);

        GameBase ongoingGame = GameStorage.get().findOngoingGame(client.uuid);
        CreditReservation reservation = new CreditReservation(client, reserve);
        if (ongoingGame != null)
            return reservation.setOngoingGame(ongoingGame).setRejected(CreditReservationRejection.ALREADY_IN_GAME);

        if (client.credits < reserve) return reservation.setRejected(CreditReservationRejection.NOT_ENOUGH_CREDITS);

        return reservation;
    }

    @Override
    public String getName() {
        return "API";
    }
}
