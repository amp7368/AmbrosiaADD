package com.ambrosia.add.discord.commands.operation;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.database.operation.TransactionEntity;
import com.ambrosia.add.database.operation.TransactionStorage;
import com.ambrosia.add.database.operation.TransactionType;
import com.ambrosia.add.discord.log.DiscordLog;
import com.ambrosia.add.discord.util.BaseSubCommand;
import com.ambrosia.add.discord.util.Emeralds;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.Nullable;

public abstract class CommandOperation extends BaseSubCommand {

    @Override
    public void onCheckedCommand(SlashCommandInteractionEvent event) {
        if (this.isBadPermission(event)) return;
        @Nullable Integer amount = findOptionAmount(event);
        if (amount == null) return;
        @Nullable ClientEntity client = findClient(event);
        if (client == null) return;
        long conductorId = event.getUser().getIdLong();
        if (sign() < 0 && amount > client.credits) {
            String msg = String.format("%s cannot afford losing %s credits.\nBalance: %d", client.displayName,
                Emeralds.longMessage(amount), client.credits);
            event.replyEmbeds(error(msg)).queue();
            return;
        }
        int change = sign() * amount;
        long clientUUID = client.uuid;
        TransactionEntity operation = TransactionStorage.get().createOperation(conductorId, clientUUID, change, operationReason());
        client = ClientStorage.get().findByUUID(clientUUID);
        if (client == null) throw new IllegalStateException(clientUUID + " is not a valid client!");
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
