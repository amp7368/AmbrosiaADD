package com.ambrosia.add.database.client;

import java.sql.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class ClientEntity {

    @Id
    @Column
    public UUID uuid;
    @Column(unique = true)
    public String displayName;
    @Column
    public Date dateCreated;

    @Column
    public long credits;
    @Column
    public long creator;

    public ClientEntity(String displayName, long creator) {
        this.uuid = UUID.randomUUID();
        this.displayName = displayName;
        this.dateCreated = new Date(System.currentTimeMillis());
        this.credits = 0;
        this.creator = creator;

    }

    public ClientEntity() {

    }
}
