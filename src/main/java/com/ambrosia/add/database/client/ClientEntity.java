package com.ambrosia.add.database.client;

import com.ambrosia.add.database.casino.game.CasinoGamesCount;
import com.ambrosia.add.database.operation.TransactionStorage;
import com.ambrosia.add.database.operation.TransactionType;
import io.ebean.DB;
import io.ebean.Model;
import io.ebean.Transaction;
import io.ebean.annotation.Identity;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "client_entity")
public class ClientEntity extends Model {

    @Id
    @Identity(start = 1000)
    public long id;

    @Embedded(prefix = "minecraft_")
    public ClientMinecraftDetails minecraft;

    @Embedded(prefix = "discord_")
    public ClientDiscordDetails discord;

    @Column(unique = true, nullable = false)
    public String displayName;
    @Column(nullable = false)
    public Timestamp dateCreated;

    @Column(nullable = false)
    public long credits;
    @Column(nullable = false)
    public long creator;

    public transient Map<TransactionType, Long> totals = new HashMap<>();
    public transient CasinoGamesCount games;

    public ClientEntity(long creator, String displayName) {
        this.displayName = displayName;
        this.dateCreated = Timestamp.from(Instant.now());
        this.credits = 0;
        this.creator = creator;
    }

    public ClientEntity() {
    }

    public void addCredits(TransactionType transactionType, long add) {
        this.totals.compute(transactionType, (k, a) -> a == null ? add : add + a);
        this.credits += add;
    }

    public void setDiscord(ClientDiscordDetails discord) {
        this.discord = discord;
    }

    public void setMinecraft(ClientMinecraftDetails minecraft) {
        this.minecraft = minecraft;
    }

    public boolean trySave() {
        try (Transaction transaction = DB.getDefault().beginTransaction()) {
            if (!DB.getDefault().checkUniqueness(this, transaction).isEmpty()) return false;
            super.save(transaction);
            transaction.commit();
            return true;
        }
    }

    public long total(TransactionType operationType) {
        return Math.abs(this.totals.getOrDefault(operationType, 0L));
    }

    public boolean hasAnyTransactions() {
        return !TransactionStorage.get().findTransactions(this.id).isEmpty();
    }

    public String bestImgUrl() {
        if (minecraft == null) {
            if (discord == null) {
                return null;
            }
            return discord.avatarUrl;
        }
        return minecraft.skinUrl();
    }

    public <T> T minecraft(Function<ClientMinecraftDetails, T> fn, T defaultIfNull) {
        return this.minecraft == null ? defaultIfNull : fn.apply(this.minecraft);
    }

    public <T> T discord(Function<ClientDiscordDetails, T> fn, T defaultIfNull) {
        return this.discord == null ? defaultIfNull : fn.apply(this.discord);
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
