package com.ambrosia.add.discord.operation;

import com.ambrosia.add.discord.operation.add.CommandDeposit;
import com.ambrosia.add.discord.operation.add.CommandWin;
import com.ambrosia.add.discord.operation.minus.CommandLoss;
import com.ambrosia.add.discord.operation.minus.CommandWithdraw;
import java.util.List;
import discord.util.dcf.slash.DCFSlashCommand;
import discord.util.dcf.slash.DCFSlashSubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandCash extends DCFSlashCommand {

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("cash", "Change a client's amount of cash").setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public List<DCFSlashSubCommand> getSubCommands() {
        return List.of(new CommandDeposit(), new CommandWin(), new CommandLoss(), new CommandWithdraw());
    }
}
