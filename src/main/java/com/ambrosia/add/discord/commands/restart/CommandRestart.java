package com.ambrosia.add.discord.commands.restart;

import com.ambrosia.add.database.game.GameBase;
import com.ambrosia.add.database.game.GameStorage;
import com.ambrosia.add.discord.util.BaseCommand;
import java.util.Map;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandRestart extends BaseCommand {

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
        Map<Long, GameBase> ongoingGames = GameStorage.get().getOngoingGames();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Ongoing Games");
        for (GameBase game : ongoingGames.values()) {
//            embed.addField(game.getName(), game.getClient().displayName, false);
//            game.getChannel()
        }

    }

    @Override
    public boolean isOnlyManager() {
        return super.isOnlyManager();
    }

    @Override
    public SlashCommandData getData() {
        return null;
    }
}
