package com.ambrosia.add.database.game;

import com.ambrosia.add.api.CreditReservation;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.database.operation.CreateTransactionException;
import com.ambrosia.add.database.operation.TransactionEntity;
import com.ambrosia.add.database.operation.TransactionStorage;
import com.ambrosia.add.database.operation.TransactionType;
import com.ambrosia.add.discord.log.DiscordLog;
import io.ebean.Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

public class GameStorage {

    private static GameStorage instance;
    private final List<Consumer<GameBase>> startHooks = new ArrayList<>();
    private final Map<Long, GameBase> clientToGames = new HashMap<>();

    public GameStorage() {
        instance = this;
    }

    public static GameStorage get() {
        return instance;
    }

    public void startGame(GameBase startedGame) {
        synchronized (this.clientToGames) {
            this.clientToGames.put(startedGame.getClient().id, startedGame);
        }
        startHooks.forEach(hook -> hook.accept(startedGame));
    }

    @Nullable
    public GameBase findOngoingGame(long id) {
        synchronized (this.clientToGames) {
            return this.clientToGames.get(id);
        }
    }

    public void endGame(CreditReservation reservation, GameResultEntity result, Transaction transaction) {
        long client = reservation.getClient().id;
        synchronized (this.clientToGames) {
            this.clientToGames.remove(client);
        }
        if (result.originalBet == 0) return;
        TransactionType transactionType = result.deltaWinnings < 0 ? TransactionType.LOSS : TransactionType.WIN;
        TransactionEntity emeraldTransaction;
        try {
            emeraldTransaction = TransactionStorage.get()
                .createOperation(0L, client, result.deltaWinnings, transactionType);
        } catch (CreateTransactionException e) {
            throw new RuntimeException(e);
        }
        DiscordLog.log().operation(ClientStorage.get().findById(client), emeraldTransaction);
        result.transaction = emeraldTransaction;
        result.save(transaction);
    }

    public Map<Long, GameBase> getOngoingGames() {
        synchronized (this.clientToGames) {
            return new HashMap<>(this.clientToGames);
        }
    }

    public void addStartHook(Consumer<GameBase> hook) {
        this.startHooks.add(hook);
    }
}
