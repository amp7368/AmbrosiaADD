package com.ambrosia.add.discord.operation;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.operation.TransactionEntity;
import com.ambrosia.add.database.operation.TransactionStorage;
import com.ambrosia.add.database.operation.TransactionType;
import com.ambrosia.add.discord.log.DiscordLog;
import com.ambrosia.add.discord.util.CommandBuilder;
import discord.util.dcf.slash.DCFSlashSubCommand;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.Nullable;

public abstract class CommandOperation extends DCFSlashSubCommand implements CommandBuilder {

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        if (this.isBadPermission(event)) return;
        @Nullable Integer amount = findOptionAmount(event);
        if (amount == null) return;
        @Nullable ClientEntity client = findClient(event);
        if (client == null) return;
        long conductorId = event.getUser().getIdLong();
        if (sign() < 0 && amount > client.credits) {
            String msg = String.format("%s cannot afford losing %d credits.\nBalance: %d", client.displayName, amount, client.credits);
            event.replyEmbeds(error(msg)).queue();
            return;
        }
        int change = sign() * amount;
        TransactionEntity operation = TransactionStorage.get().createOperation(conductorId, client.uuid, change, operationReason());
        event.replyEmbeds(embedClientProfile(client, operation.display())).queue();
        DiscordLog.log().operation(client, operation, event.getUser());
    }


    protected abstract int sign();

    @Override
    public SubcommandData getData() {
        SubcommandData command = new SubcommandData(commandName(), "Deposits credits to a profile");
        addOptionProfileName(command);
        addOptionAmount(command);
        return command;
    }

    protected abstract MessageEmbed successMessage(ClientEntity client, int amount);

    protected abstract TransactionType operationReason();

    protected abstract String commandName();

}
