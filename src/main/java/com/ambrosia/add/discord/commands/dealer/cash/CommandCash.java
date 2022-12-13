package com.ambrosia.add.discord.commands.dealer.cash;

import com.ambrosia.add.discord.commands.dealer.cash.add.CommandAward;
import com.ambrosia.add.discord.commands.dealer.cash.add.CommandDeposit;
import com.ambrosia.add.discord.commands.dealer.cash.add.CommandWin;
import com.ambrosia.add.discord.commands.dealer.cash.minus.CommandLoss;
import com.ambrosia.add.discord.commands.dealer.cash.minus.CommandWithdraw;
import com.ambrosia.add.discord.util.BaseCommand;
import discord.util.dcf.slash.DCFSlashSubCommand;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandCash extends BaseCommand {

    @Override
    public void onCheckedCommand(SlashCommandInteractionEvent event) {
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("cash", "Change a client's amount of cash").setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }
    @Override
    public boolean isOnlyDealer() {
        return true;
    }
    @Override
    public List<DCFSlashSubCommand> getSubCommands() {
        return List.of(new CommandDeposit(), new CommandWin(), new CommandLoss(), new CommandWithdraw(), new CommandAward());
    }
}
