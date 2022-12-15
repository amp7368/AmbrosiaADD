package com.ambrosia.add.discord.active.base;

import discord.util.dcf.gui.stored.IDCFStoredDormantGui;

public abstract class ActiveRequest implements IDCFStoredDormantGui {

    public String typeId;
    public long messageId;
    public ActiveRequestStage stage = ActiveRequestStage.CREATED;

    public ActiveRequest(String typeId) {
        this.typeId = typeId;
    }

    @Override
    public long getId() {
        return this.messageId;
    }

    @Override
    public int hashCode() {
        return (int) (this.messageId % Integer.MAX_VALUE);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ActiveRequest other && other.messageId == this.messageId;
    }
}
