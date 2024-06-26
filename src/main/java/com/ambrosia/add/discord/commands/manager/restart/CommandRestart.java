package com.ambrosia.add.discord.commands.manager.restart;

import apple.utilities.util.Pretty;
import com.ambrosia.add.Ambrosia;
import com.ambrosia.add.database.game.GameBase;
import com.ambrosia.add.database.game.GameStorage;
import com.ambrosia.add.discord.log.RestartingMessageManager;
import com.ambrosia.add.discord.util.BaseCommand;
import java.awt.Color;
import java.util.Map;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandRestart extends BaseCommand {

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
        Boolean force = findOption(event, "force", OptionMapping::getAsBoolean, false);
        if (force != null && force) {
            event.reply("Disabling (doesn't actually restart the bot. Just disable and re-enable, so it might not fix things)")
                .queue();
            Ambrosia.get().onDisable_();
            new Thread(Ambrosia.get()::start).start();
            return;
        }
        Map<Long, GameBase> ongoingGames = GameStorage.get().getOngoingGames();
        MessageEmbed embed = makeMessage(ongoingGames);
        for (GameBase game : ongoingGames.values()) {
            RestartingMessageManager.get().sendRestarting(game.getChannel());
        }
        event.replyEmbeds(embed).queue((message) -> {
            ongoingGames.values().forEach(game -> game.addFinishHook((g) -> edit(message)));
            GameStorage.get().addStartHook((game) -> {
                game.addFinishHook((g) -> edit(message));
                RestartingMessageManager.get().sendRestarting(game.getChannel());
                this.edit(message);
            });
        });
    }

    private void edit(InteractionHook message) {
        message.editOriginalEmbeds(makeMessage(GameStorage.get().getOngoingGames())).queue();
    }

    private MessageEmbed makeMessage(Map<Long, GameBase> ongoingGames) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(String.format("Ongoing Games (%d)", ongoingGames.size()));
        embed.setColor(Color.RED);
        for (GameBase game : ongoingGames.values()) {
            embed.addField(Pretty.spaceEnumWords(game.getName()),
                game.getClient().displayName + " in " + game.getChannel().getAsMention(), false);
        }
        return embed.build();
    }

    @Override
    public boolean isOnlyManager() {
        return true;
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("restart", "Message ongoing players that the bot will restart soon.")
            .addOption(OptionType.BOOLEAN, "force", "Force the bot to 'restart'");
    }
}
