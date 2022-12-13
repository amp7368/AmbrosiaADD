package com.ambrosia.add.discord.commands.dealer.cash.minus;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.operation.TransactionType;
import com.ambrosia.add.discord.commands.dealer.cash.CommandOperation;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

public class CommandLoss extends CommandOperation {

    @Override
    protected MessageEmbed successMessage(ClientEntity client, int amount) {
        return this.success(String.format("%s loses %d credits\nNew balance: %d", client.displayName, amount, client.credits));
    }

    @Override
    protected TransactionType operationReason() {
        return TransactionType.LOSS;
    }

    @Override
    public boolean isOnlyDealer() {
        return true;
    }

    @NotNull
    protected String commandName() {
        return "loss";
    }

    protected int sign() {
        return -1;
    }
}
