package com.ambrosia.add.discord.active.cash;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.operation.TransactionStorage;
import com.ambrosia.add.database.operation.TransactionType;
import com.ambrosia.add.discord.active.ActiveRequestType;
import com.ambrosia.add.discord.active.base.ActiveRequest;
import com.ambrosia.add.discord.active.base.ActiveRequestSender;
import net.dv8tion.jda.api.entities.Member;

public class ActiveRequestCash extends ActiveRequest<ActiveRequestCashGui> {

    private final int amount;
    private final long clientId;

    private final TransactionType transactionType;

    public ActiveRequestCash(Member sender, ClientEntity client, int amount, TransactionType transactionType, long clientId) {
        super(ActiveRequestType.CASH.getTypeId(), new ActiveRequestSender(sender, client));
        this.amount = amount;
        this.transactionType = transactionType;
        this.clientId = clientId;
    }

    @Override
    public ActiveRequestCashGui load() {
        return new ActiveRequestCashGui(messageId, this);
    }

    public void onApprove() throws Exception {
        TransactionStorage.get().createOperation(this.getEndorserId(), clientId, amount, transactionType);
    }

    @Override
    public void onComplete() throws Exception {
    }

    public TransactionType transactionType() {
        return this.transactionType;
    }

    public int getAmount() {
        return amount;
    }

}
