package com.ambrosia.add.database.client;

import java.sql.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ClientEntity {

    @Id
    @Column
    public long uuid;

    @Column(unique = true)
    public UUID minecraft;
    @Column(unique = true)
    public Long discord;
    @Column(unique = true, nullable = false)
    public String displayName;
    @Column(nullable = false)
    public Date dateCreated;

    @Column(nullable = false)
    public long credits;
    @Column(nullable = false)
    public long creator;

    public ClientEntity(long creator, String displayName) {
        this.displayName = displayName;
        this.dateCreated = new Date(System.currentTimeMillis());
        this.credits = 0;
        this.creator = creator;
    }

    public ClientEntity() {

    }

    public void setCredits(long credits) {
        this.credits = credits;
    }
}
