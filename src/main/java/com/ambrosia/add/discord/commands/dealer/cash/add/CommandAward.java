package com.ambrosia.add.discord.commands.dealer.cash.add;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.operation.TransactionType;
import com.ambrosia.add.discord.commands.dealer.cash.CommandOperation;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

public class CommandAward extends CommandOperation {
    @Override
    protected MessageEmbed successMessage(ClientEntity client, int amount) {
        return this.success(
            String.format("%s is awarded %d credits\nNew balance: %d", client.displayName, amount, client.credits));
    }

    @Override
    public boolean isOnlyDealer() {
        return true;
    }

    @Override
    protected TransactionType operationReason() {
        return TransactionType.AWARD;
    }

    @NotNull
    protected String commandName() {
        return "award";
    }

    protected int sign() {
        return 1;
    }
}
