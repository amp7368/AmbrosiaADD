package com.ambrosia.roulette.game.table;

import com.ambrosia.add.discord.util.BaseSubCommand;
import com.ambrosia.roulette.game.table.gui.RouletteTableGui;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class RouletteTableSubCommand extends BaseSubCommand {

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
        if (!(event.getChannel() instanceof TextChannel channel)) {
            error("Only in Ambrosia Server");
            return;
        }
        RouletteGame game = RouletteGameManager.getGame(channel);
        if (game != null) {
            game.resendGui(event::reply);
            return;
        }
        game = RouletteGameManager.createTable(channel);
        game.start(new RouletteTableGui(game, dcf, event::reply));
    }

    @Override
    public SubcommandData getData() {
        return new SubcommandData("table", "Start a new roulette game in the current channel");
    }
}
