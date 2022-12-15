package com.ambrosia.add.discord.active.base;

import discord.util.dcf.gui.stored.DCFStoredDormantGui;

public abstract class ActiveRequest<Gui extends ActiveRequestGui<?>> extends DCFStoredDormantGui<Gui> {

    public String typeId;
    public ActiveRequestStage stage = ActiveRequestStage.CREATED;
    public ActiveRequestSender sender;

    public ActiveRequest(String typeId, ActiveRequestSender sender) {
        this.typeId = typeId;
        this.sender = sender;
    }

    @Override
    public int hashCode() {
        return (int) (this.getId() % Integer.MAX_VALUE);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ActiveRequest<?> other && other.getId() == this.getId();
    }
}
