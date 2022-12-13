package com.ambrosia.add.database.game;

import com.ambrosia.add.api.CreditReservation;
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

    private final Map<Long, GameBase> clientToGames = new HashMap<>();

    public static GameStorage get() {
        return instance;
    }

    public GameBase startGame(GameBase startedGame) {
        synchronized (this.clientToGames) {
            this.clientToGames.put(startedGame.getClient().uuid, startedGame);
        }
        return startedGame;
    }

    @Nullable
    public GameBase findOngoingGame(long uuid) {
        synchronized (this.clientToGames) {
            return this.clientToGames.get(uuid);
        }
    }

    public void endGame(CreditReservation reservation, GameResultEntity result) {
        long client = reservation.getClient().uuid;
        synchronized (this.clientToGames) {
            this.clientToGames.remove(client);
        }
        if (result.originalBet == 0) return;
        TransactionType transactionType = result.deltaWinnings < 0 ? TransactionType.LOSS : TransactionType.WIN;
        TransactionEntity transaction = TransactionStorage.get()
            .createOperation(0L, client, result.deltaWinnings, transactionType);
        DiscordLog.log().operation(ClientStorage.get().findByUUID(client), transaction);
        result.transaction = transaction;
        result.save();
    }

    public Map<Long, GameBase> getOngoingGames() {
        synchronized (this.clientToGames) {
            return new HashMap<>(this.clientToGames);
        }
    }
}
