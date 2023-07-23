package com.ambrosia.roulette.game.table;

import com.ambrosia.add.discord.util.BaseSubCommand;
import com.ambrosia.roulette.game.game.RouletteGame;
import com.ambrosia.roulette.game.game.RouletteGameManager;
import com.ambrosia.roulette.game.table.gui.RouletteTableGui;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class RouletteTableSubCommand extends BaseSubCommand {

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
        if (!(event.getChannel() instanceof TextChannel channel)) {
            error("Only in TextChannels");
            return;
        }
        RouletteGame game = RouletteGameManager.getGame(channel.getIdLong());
        if (game != null) {
            error("There is already an ongoing game in this channel! Use `/roulette bet` to join.");
            return;
        }
        game = RouletteGameManager.createTable(channel);
        game.start(new RouletteTableGui(game, dcf, event::reply));
    }

    @Override
    public boolean isOnlyDealer() {
        return true;
    }

    @Override
    public SubcommandData getData() {
        return new SubcommandData("table", "Start a new roulette game in the current channel");
    }
}
