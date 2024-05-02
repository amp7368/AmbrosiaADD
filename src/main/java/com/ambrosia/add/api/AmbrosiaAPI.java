package com.ambrosia.add.api;

import apple.lib.modules.AppleModule;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.database.game.GameStorage;

public class AmbrosiaAPI extends AppleModule {

    private static AmbrosiaAPI instance;

    public AmbrosiaAPI() {
        instance = this;
    }

    public static AmbrosiaAPI get() {
        return instance;
    }

    public synchronized CreditReservation reserve(long discordId, long reserve, boolean allowAlreadyPlaying) {
        ClientEntity client = ClientStorage.get().findByDiscord(discordId);
        if (client == null) {
            if (reserve == 0) {
                ClientEntity guest = new ClientEntity(0, "Guest");
                return new CreditReservation(guest, reserve);
            }
            return new CreditReservation(null, reserve)
                .setRejected(CreditReservationRejection.NO_CLIENT);
        }

        CreditReservation reservation = new CreditReservation(client, reserve);
        reservation.setOngoingGame(GameStorage.get().findOngoingGame(client.id));

        if (!allowAlreadyPlaying && reservation.alreadyPlaying())
            return reservation.setRejected(CreditReservationRejection.ALREADY_IN_GAME);

        if (reservation.notEnoughCredits())
            return reservation.setRejected(CreditReservationRejection.NOT_ENOUGH_CREDITS);

        return reservation;
    }

    @Override
    public String getName() {
        return "API";
    }
}
