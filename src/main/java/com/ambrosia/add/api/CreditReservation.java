package com.ambrosia.add.api;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.game.GameResultEntity;
import com.ambrosia.add.database.game.GameStorage;
import com.ambrosia.add.database.game.OngoingGame;

public class CreditReservation {

    private final ClientEntity client;
    private final long reserved;
    private OngoingGame ongoingGame;
    private CreditReservationRejection rejection;

    public CreditReservation(ClientEntity client, long reserved) {
        this.client = client;
        this.reserved = reserved;
    }

    public boolean notEnoughCredits() {
        return rejection == CreditReservationRejection.NOT_ENOUGH_CREDITS;
    }

    public boolean noPlayer() {
        return this.rejection == CreditReservationRejection.NO_CLIENT;
    }

    public boolean alreadyPlaying() {
        return this.rejection == CreditReservationRejection.ALREADY_IN_GAME;
    }

    public boolean isRejected() {
        return this.rejection != null;
    }

    public CreditReservation setRejected(CreditReservationRejection rejection) {
        this.rejection = rejection;
        return this;
    }

    public ClientEntity getClient() {
        return this.client;
    }

    public long getReserved() {
        return reserved;
    }

    public long getClientCredits() {
        return this.client == null ? 0 : client.credits;
    }


    public CreditReservation setOngoingGame(OngoingGame ongoingGame) {
        this.ongoingGame = ongoingGame;
        return this;
    }

    public void release(GameResultEntity result) {
        GameStorage.get().endGame(this, result);
    }
}
