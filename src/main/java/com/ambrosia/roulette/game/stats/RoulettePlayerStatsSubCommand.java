package com.ambrosia.roulette.game.stats;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.discord.util.AmbrosiaColor;
import com.ambrosia.add.discord.util.CommandBuilder;
import com.ambrosia.roulette.game.player.RoulettePlayerGame;
import com.ambrosia.roulette.game.player.RoulettePlayerWinnings;
import com.ambrosia.roulette.game.table.RouletteGame;
import com.ambrosia.roulette.game.table.RouletteGameManager;
import com.ambrosia.roulette.img.RouletteImage;
import discord.util.dcf.slash.DCFSlashSubCommand;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RoulettePlayerStatsSubCommand extends DCFSlashSubCommand implements CommandBuilder {

    @Override
    public SubcommandData getData() {
        return new SubcommandData("pstats", "Show your winning/losing bets for a roulette game")
            .addOption(OptionType.INTEGER, "game_id", "The id of the game to view");
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        RouletteGame game = findGame(event);
        if (game == null) return;
        RoulettePlayerGame player = findPlayer(event, game);
        if (player == null) return;

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(AmbrosiaColor.CASINO_COLOR);
        String title = "Landed on %s!".formatted(game.getSpinResult().display(false, true));
        embed.setTitle(title);
        embed.setAuthor(player.getPlayerName(), null, player.getClient().bestImgUrl());
        RoulettePlayerWinnings winnings = player.getWinnings();
        String summary = winnings.shortSummaryField().getValue();
        String winningsDesc = joinBets(winnings.getWinningBetsDescriptions());
        String lossesDesc = joinBets(winnings.getLosingBetsDescriptions());

        String desc = """
            # Summary
            %s
                        
            # Winning bets:
            %s
                        
            # Losing bets:
            %s
            """.formatted(summary, winningsDesc, lossesDesc);
        embed.setDescription(desc);
        embed.setImage(RouletteImage.IMAGE_BETTING_TABLE);
        event.replyEmbeds(embed.build()).queue();
    }

    @NotNull
    private String joinBets(List<String> bets) {
        return bets.isEmpty() ? "---\n" : String.join("\n", bets);
    }

    @Nullable
    private RoulettePlayerGame findPlayer(SlashCommandInteractionEvent event, RouletteGame game) {
        ClientEntity client = ClientStorage.get().findByDiscord(event.getUser().getIdLong());
        if (client == null) {
            errorRegisterWithStaff(event);
            return null;
        }
        RoulettePlayerGame player = game.getPlayer(client);
        if (player == null) {
            event.replyEmbeds(error("You did not play in this game!"))
                .setEphemeral(true)
                .queue();
            return null;
        }
        return player;
    }

    @Nullable
    private RouletteGame findGame(SlashCommandInteractionEvent event) {
        Integer gameId = findOption(event, "game_id", OptionMapping::getAsInt, true);
        if (gameId == null) return null;
        RouletteGame game = RouletteGameManager.getGame(gameId);
        if (game == null) {
            MessageEmbed errorMessage = error("Game #%d does not exist, or is too old to retrieve player stats".formatted(gameId));
            event.replyEmbeds(errorMessage)
                .setEphemeral(true)
                .queue();
            return null;
        }
        return game;
    }
}
