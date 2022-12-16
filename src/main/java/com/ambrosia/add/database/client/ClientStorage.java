package com.ambrosia.add.database.client;

import com.ambrosia.add.database.casino.GameResultAggregate;
import com.ambrosia.add.database.casino.game.CasinoGamesCount;
import com.ambrosia.add.database.casino.query.QGameResultAggregate;
import com.ambrosia.add.database.client.query.QClientEntity;
import com.ambrosia.add.database.operation.TransactionEntity;
import com.ambrosia.add.database.operation.TransactionStorage;
import com.ambrosia.add.discord.active.account.UpdateAccountException;
import io.ebean.DB;
import io.ebean.Transaction;
import io.ebean.plugin.Property;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.Nullable;

public class ClientStorage {

    private static ClientStorage instance;
    private final Map<Long, ClientEntity> allClients = new HashMap<>();

    public ClientStorage() {
        instance = this;
        List<ClientEntity> clients = new QClientEntity().findList();
        for (ClientEntity client : clients) {
            updateClient(client);
        }
    }

    public static ClientStorage get() {
        return instance;
    }

    public void updateClient(ClientEntity client) {
        synchronized (allClients) {
            this.allClients.put(client.uuid, client);
        }
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
        updateClient(client);
        return fillInClient(client);
    }

    public ClientEntity createClient(String displayName, ClientDiscordDetails discord) throws UpdateAccountException {
        ClientEntity client = new ClientEntity(0, displayName);
        client.setDiscord(discord);
        try (Transaction transaction = DB.getDefault().beginTransaction()) {
            Set<Property> uniqueness = DB.getDefault().checkUniqueness(client, transaction);
            if (!uniqueness.isEmpty()) {
                List<String> badProperties = new ArrayList<>();
                for (Property property : uniqueness) {
                    String format = String.format("'%s' is not unique! Provided: '%s'", property.name(), property.value(client));
                    badProperties.add(format);
                }
                throw new UpdateAccountException(String.join(", ", badProperties));
            }
            client.save(transaction);
            transaction.commit();
        }
        updateClient(client);
        return fillInClient(client);
    }

    @Nullable
    public ClientEntity findByUUID(long uuid) {
        ClientEntity client = new QClientEntity().where().uuid.eq(uuid).findOne();
        if (client == null) return null;
        return fillInClient(client);
    }

    public Stream<ClientEntity> allNames() {
        synchronized (allClients) {
            return allClients.values().stream();
        }
    }
}
