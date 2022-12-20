package com.ambrosia.roulette.bet;

import com.ambrosia.add.discord.util.BaseCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class RouletteBetCommand extends BaseCommand {

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
        RoulettePlayerGui gui = new RoulettePlayerGui(dcf, event::reply);
        new RoulettePlayerGuiPage(gui);
        gui.send();
    }

    @Override
    public boolean isOnlyDealer() {
        return true;
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("bet", "bet");
    }
}
