package com.ambrosia.add.discord.commands.operation.add;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.operation.TransactionType;
import com.ambrosia.add.discord.commands.operation.CommandOperation;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

public class CommandDeposit extends CommandOperation {

    @Override
    protected MessageEmbed successMessage(ClientEntity client, int amount) {
        return this.success(
            String.format("%s deposits %d credits into their account\nNew balance: %d", client.displayName, amount, client.credits));
    }

    @Override
    public boolean isOnlyDealer() {
        return true;
    }

    @Override
    protected TransactionType operationReason() {
        return TransactionType.DEPOSIT;
    }

    @NotNull
    protected String commandName() {
        return "deposit";
    }

    protected int sign() {
        return 1;
    }
}
