package com.ambrosia.add.discord.create;

import com.ambrosia.add.apple.discord.lib.DCFSlashCommand;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CreateProfileCommand extends DCFSlashCommand {

    public CreateProfileCommand() {
    }

    public SlashCommandData getData() {
        return Commands.slash("create", "Create a profile for a customer")
            .addOption(OptionType.STRING, "name", "The name of the customer", true)
            .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }
}
