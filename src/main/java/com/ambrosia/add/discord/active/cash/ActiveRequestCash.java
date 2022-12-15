package com.ambrosia.add.discord.active.cash;

import com.ambrosia.add.discord.active.ActiveRequestType;
import com.ambrosia.add.discord.active.base.ActiveRequest;

public class ActiveRequestCash extends ActiveRequest<ActiveRequestCashGui> {

    public ActiveRequestCash() {
        super(ActiveRequestType.CASH.getTypeId(), null);
    }

    @Override
    public ActiveRequestCashGui load() {
        return new ActiveRequestCashGui(messageId, this);
    }

}
