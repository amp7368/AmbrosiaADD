package com.ambrosia.add.discord.commands.player.request;

import com.ambrosia.add.database.operation.TransactionType;

public class CommandRequestWithdraw extends CommandRequestCash {

    @Override
    protected int sign() {
        return -1;
    }

    @Override
    protected TransactionType transactionType() {
        return TransactionType.WITHDRAW;
    }
}
