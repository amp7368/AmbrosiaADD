package com.ambrosia.add.database.game;

import com.ambrosia.add.api.CreditReservation;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.database.operation.TransactionEntity;
import com.ambrosia.add.database.operation.TransactionStorage;
import com.ambrosia.add.database.operation.TransactionType;
import com.ambrosia.add.discord.log.DiscordLog;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public class GameStorage {

    private static GameStorage instance;

    public GameStorage() {
        instance = this;
    }

    private final Map<Long, OngoingGame> clientToGames = new HashMap<>();

    public static GameStorage get() {
        return instance;
    }

    public OngoingGame startGame(ClientEntity client, String name) {
        OngoingGame startedGame = new OngoingGame(name);
        synchronized (this.clientToGames) {
            this.clientToGames.put(client.uuid, startedGame);
        }
        return startedGame;
    }

    @Nullable
    public OngoingGame findOngoingGame(long uuid) {
        synchronized (this.clientToGames) {
            return this.clientToGames.get(uuid);
        }
    }

    public void endGame(CreditReservation reservation, GameResultEntity result) {
        TransactionType transactionType = result.deltaWinnings < 0 ? TransactionType.LOSS : TransactionType.WIN;
        long client = reservation.getClient().uuid;
        TransactionEntity transaction = TransactionStorage.get()
            .createOperation(0L, client, result.deltaWinnings, transactionType);
        DiscordLog.log().operation(ClientStorage.get().findByUUID(client), transaction);
        result.transactionId = transaction.id;
        synchronized (this.clientToGames) {
            this.clientToGames.remove(client);
        }
        result.save();
    }
}
