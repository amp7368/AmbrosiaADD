package com.ambrosia.add.discord.commands.player.request;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.database.operation.TransactionType;
import com.ambrosia.add.discord.active.ActiveRequestDatabase;
import com.ambrosia.add.discord.active.cash.ActiveRequestCash;
import com.ambrosia.add.discord.active.cash.ActiveRequestCashGui;
import com.ambrosia.add.discord.util.BaseSubCommand;
import java.util.Locale;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public abstract class CommandRequestCash extends BaseSubCommand {

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
        Integer amount = findOptionAmount(event);
        if (amount == null) return;
        if (amount == 0) {
            event.replyEmbeds(error("Total must be > 0")).setEphemeral(true).queue();
            return;
        }
        Member member = event.getMember();
        if (member == null) return;
        ClientEntity client = ClientStorage.get().findByDiscord(member.getIdLong());
        if (client == null) {
            event.replyEmbeds(error("Create an account first. /request account")).setEphemeral(true).queue();
            return;
        }
        if (sign() < 0 && client.credits < amount) {
            event.replyEmbeds(error("Not enough credits")).setEphemeral(true).queue();
            return;
        }
        ActiveRequestCash request = new ActiveRequestCash(member, client, sign() * amount, transactionType(), client.id);

        ActiveRequestCashGui gui = request.create();
        event.reply(gui.makeClientMessage()).queue();
        gui.send(ActiveRequestDatabase::sendRequest);
    }

    protected abstract int sign();

    protected abstract TransactionType transactionType();

    @Override
    public SubcommandData getData() {
        String name = transactionType().displayName();
        String description = String.format("Request to %s emerald(s)", name);
        SubcommandData command = new SubcommandData(name.toLowerCase(Locale.ROOT), description);
        addOptionAmount(command);
        return command;
    }

}
