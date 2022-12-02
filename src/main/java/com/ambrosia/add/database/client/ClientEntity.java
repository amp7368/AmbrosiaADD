package com.ambrosia.add.database.client;

import com.ambrosia.add.database.operation.OperationReason;
import io.ebean.DB;
import io.ebean.Model;
import io.ebean.Transaction;
import io.ebean.annotation.Identity;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ClientEntity extends Model {

    @Id
    @Column
    @Identity(start = 1000)
    public long uuid;

    @Column
    @Embedded
    public ClientMinecraftDetails minecraft;
    @Column
    @Embedded
    public ClientDiscordDetails discord;

    @Column(unique = true, nullable = false)
    public String displayName;
    @Column(nullable = false)
    public Timestamp dateCreated;

    @Column(nullable = false)
    public long credits;
    @Column(nullable = false)
    public long creator;

    public transient Map<OperationReason, Long> totals = new HashMap<>();

    public ClientEntity(long creator, String displayName) {
        this.displayName = displayName;
        this.dateCreated = Timestamp.from(Instant.now());
        this.credits = 0;
        this.creator = creator;
    }

    public ClientEntity() {

    }

    public void addCredits(OperationReason operationReason, long add) {
        this.totals.compute(operationReason, (k, a) -> a == null ? add : add + a);
        this.credits += add;
    }

    public void setDiscord(ClientDiscordDetails discord) {
        this.discord = discord;
        this.displayName = discord.guildName;
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

    public long total(OperationReason operationType) {
        return Math.abs(this.totals.getOrDefault(operationType, 0L));
    }
}
