package com.ambrosia.add.discord.active.base;

import discord.util.dcf.gui.stored.DCFStoredDormantGui;
import net.dv8tion.jda.api.entities.User;

public abstract class ActiveRequest<Gui extends ActiveRequestGui<?>> extends DCFStoredDormantGui<Gui> {

    public String typeId;
    public ActiveRequestStage stage = ActiveRequestStage.CREATED;
    public ActiveRequestSender sender;
    private String endorser;
    private long endorserId;

    public ActiveRequest(String typeId, ActiveRequestSender sender) {
        this.typeId = typeId;
        this.sender = sender;
    }

    @Override
    public int hashCode() {
        return (int) (this.getMessageId() % Integer.MAX_VALUE);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ActiveRequest<?> other && other.getMessageId() == this.getMessageId();
    }

    public String getEndorser() {
        return endorser;
    }

    public void setEndorser(User endorser) {
        this.endorser = endorser.getName();
        this.endorserId = endorser.getIdLong();
    }

    public long getEndorserId() {
        return endorserId;
    }

    public abstract void onComplete() throws Exception;
}
