package com.ambrosia.add.discord.active.cash;

import com.ambrosia.add.discord.active.base.ActiveRequest;
import com.ambrosia.add.discord.active.ActiveRequestType;
import discord.util.dcf.gui.stored.DCFStoredGui;

public class ActiveRequestCash extends ActiveRequest {

    public ActiveRequestCash() {
        super(ActiveRequestType.CASH.getTypeId());
    }

    @Override
    public DCFStoredGui load(long messageId) {
        return new ActiveRequestCashGui(messageId);
    }

}
