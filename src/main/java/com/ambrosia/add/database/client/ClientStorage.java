package com.ambrosia.add.database.client;

import com.ambrosia.add.database.casino.GameResultAggregate;
import com.ambrosia.add.database.casino.game.CasinoGamesCount;
import com.ambrosia.add.database.casino.query.QGameResultAggregate;
import com.ambrosia.add.database.client.query.QClientEntity;
import com.ambrosia.add.database.operation.TransactionEntity;
import com.ambrosia.add.database.operation.TransactionStorage;
import io.ebean.DB;
import io.ebean.Transaction;
import java.util.HashMap;
import java.util.List;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.Nullable;

public class ClientStorage {

    private static ClientStorage instance;

    public ClientStorage() {
        instance = this;
    }

    public static ClientStorage get() {
        return instance;
    }

    private ClientEntity fillInClient(ClientEntity client) {
        List<TransactionEntity> byOperationType = TransactionStorage.get().aggregateByType(client);
        client.totals = new HashMap<>();
        for (TransactionEntity aggregation : byOperationType) {
            client.totals.put(aggregation.operationType, aggregation.sumAmount);
        }
        QGameResultAggregate alias = QGameResultAggregate.alias();
        List<GameResultAggregate> games = new QGameResultAggregate().select(alias.count, alias.conclusion, alias.name,
            alias.deltaWinnings).findList();
        client.games = new CasinoGamesCount(games);
        return client;
    }

    public ClientEntity findByName(String clientName) {
        ClientEntity client = new QClientEntity().where().displayName.ieq(clientName).findOne();
        if (client == null) return null;
        return fillInClient(client);
    }


    public ClientEntity findByDiscord(long discordId) {
        ClientEntity client = new QClientEntity().where().discord.discordId.eq(discordId).findOne();
        if (client == null) return null;
        return fillInClient(client);
    }

    @Nullable
    public ClientEntity createClient(long conductorId, String clientName, Member discord) {
        ClientEntity client = new ClientEntity(conductorId, clientName);
        client.setMinecraft(ClientMinecraftDetails.fromUsername(clientName));
        if (discord != null) client.setDiscord(new ClientDiscordDetails(discord));
        try (Transaction transaction = DB.getDefault().beginTransaction()) {
            boolean isUnique = DB.getDefault().checkUniqueness(client, transaction).isEmpty();
            if (!isUnique) return null;
            client.save(transaction);
            transaction.commit();
        }
        return fillInClient(client);
    }

    @Nullable
    public ClientEntity findByUUID(long uuid) {
        ClientEntity client = new QClientEntity().where().uuid.eq(uuid).findOne();
        if (client == null) return null;
        return fillInClient(client);
    }
}
