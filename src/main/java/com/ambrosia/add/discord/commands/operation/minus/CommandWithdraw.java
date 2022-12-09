package com.ambrosia.add.discord.commands.operation.minus;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.operation.TransactionType;
import com.ambrosia.add.discord.commands.operation.CommandOperation;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

public class CommandWithdraw extends CommandOperation {

    @Override
    protected MessageEmbed successMessage(ClientEntity client, int amount) {
        return this.success(
            String.format("%s withdrew %d credits from their account.\nNew balance: %d", client.displayName, amount, client.credits));
    }

    @Override
    protected TransactionType operationReason() {
        return TransactionType.WITHDRAW;
    }

    @Override
    public boolean isOnlyDealer() {
        return true;
    }

    @NotNull
    protected String commandName() {
        return "withdraw";
    }

    protected int sign() {
        return -1;
    }
}
