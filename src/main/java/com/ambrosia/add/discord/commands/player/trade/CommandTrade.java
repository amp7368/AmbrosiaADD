package com.ambrosia.add.discord.commands.player.trade;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.database.operation.TransactionStorage;
import com.ambrosia.add.discord.util.BaseCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandTrade extends BaseCommand {

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
        ClientEntity clientTrading = ClientStorage.get().findByDiscord(event.getUser().getIdLong());
        if (clientTrading == null) {
            this.errorRegisterWithStaff(event);
            return;
        }
        Integer amount = findOptionAmount(event);
        if (amount == 0) {
            event.reply("Total must be > 0").setEphemeral(true).queue();
            return;
        }
        ClientEntity clientReceiving = findClient(event);
        if (clientReceiving == null) return;
        if (clientReceiving.uuid == clientTrading.uuid) {
            event.reply("Trade to someone else").setEphemeral(true).queue();
            return;
        }
        clientTrading = TransactionStorage.get().trade(clientTrading.uuid, clientReceiving.uuid, amount);
        event.replyEmbeds(embedClientProfile(clientTrading, "Trade to " + clientReceiving.displayName)).queue();
    }

    @Override
    public SlashCommandData getData() {
        SlashCommandData command = Commands.slash("trade", "Trade emeralds to another player");
        addOptionProfileName(command);
        addOptionAmount(command);
        return command;
    }
}