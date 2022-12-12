package com.ambrosia.add.discord.commands.manager.delete;

import com.ambrosia.add.database.operation.TransactionEntity;
import com.ambrosia.add.database.operation.TransactionStorage;
import com.ambrosia.add.discord.util.BaseSubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandDeleteOperation extends BaseSubCommand {

    @Override
    public SubcommandData getData() {
        SubcommandData command = new SubcommandData("entry", "Delete an add or subtract entry");
        command.addOption(OptionType.INTEGER, "id", "The id of the entry to delete", true);
        return command;
    }

    @Override
    public void onCheckedCommand(SlashCommandInteractionEvent event) {
        Long id = findOption(event, "id", OptionMapping::getAsLong);
        if (id == null) return;
        // todo event.replyModal(Modal.create().build())
        TransactionEntity operation = TransactionStorage.get().delete(id);
        if (operation == null) {
            event.replyEmbeds(error(String.format("#%d is not a valid entry", id))).queue();
            return;
        }
        event.replyEmbeds(success(String.format("#%d has been deleted", id))).queue();
    }

    @Override
    public boolean isOnlyManager() {
        return true;
    }
}
