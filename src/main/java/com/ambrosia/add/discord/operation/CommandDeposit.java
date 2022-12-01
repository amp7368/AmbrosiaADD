package com.ambrosia.add.discord.operation;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.operation.OperationReason;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

public class CommandDeposit extends CommandOperation {

    @Override
    protected MessageEmbed successMessage(ClientEntity client, int amount) {
        return this.success(
            String.format("%s deposited %d credits into their account\nNew balance: %d", client.displayName, amount, client.credits));
    }

    @Override
    protected OperationReason operationReason() {
        return OperationReason.DEPOSIT;
    }

    @NotNull
    protected String commandName() {
        return "deposit";
    }
}
