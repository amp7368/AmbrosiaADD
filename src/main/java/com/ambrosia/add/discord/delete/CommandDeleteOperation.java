package com.ambrosia.add.discord.delete;

import com.ambrosia.add.database.operation.OperationEntity;
import com.ambrosia.add.database.operation.OperationStorage;
import com.ambrosia.add.discord.util.CommandBuilder;
import discord.util.dcf.slash.DCFSlashSubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandDeleteOperation extends DCFSlashSubCommand implements CommandBuilder {

    @Override
    public SubcommandData getData() {
        SubcommandData command = new SubcommandData("entry", "Delete an add or subtract entry");
        command.addOption(OptionType.INTEGER, "id", "The id of the entry to delete", true);
        return command;
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        Long id = findOption(event, "id", OptionMapping::getAsLong);
        if (id == null) return;
//        event.replyModal(Modal.create().build())
        OperationEntity operation = OperationStorage.get().delete(id);
        if (operation == null) {
            event.replyEmbeds(error(String.format("#%d is not a valid entry", id))).queue();
            return;
        }
        event.replyEmbeds(success(String.format("#%d has been deleted", id))).queue();
    }
}
