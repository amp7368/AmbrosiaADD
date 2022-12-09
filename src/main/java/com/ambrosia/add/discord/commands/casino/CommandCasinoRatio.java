package com.ambrosia.add.discord.commands.casino;

import com.ambrosia.add.discord.util.BaseSubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandCasinoRatio extends BaseSubCommand {

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
    }

    @Override
    public boolean isOnlyManager() {
        return true;
    }

    @Override
    public SubcommandData getData() {
        return new SubcommandData("ratio", "See the house edge");
    }
}
