package com.ambrosia.roulette.game.bet;

import com.ambrosia.add.api.CreditReservation;
import com.ambrosia.add.discord.util.BaseSubCommand;
import com.ambrosia.add.discord.util.BetCommand;
import com.ambrosia.add.discord.util.Emeralds;
import com.ambrosia.roulette.game.player.RoulettePlayerGame;
import com.ambrosia.roulette.game.player.gui.RoulettePlayerGui;
import com.ambrosia.roulette.game.player.gui.RoulettePlayerMainPage;
import com.ambrosia.roulette.game.table.RouletteGame;
import com.ambrosia.roulette.game.table.RouletteGameManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class RouletteBetSubCommand extends BaseSubCommand implements BetCommand {

    public static final int ONE_HAND_MAX_BET = Emeralds.leToEmeralds(8);

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
        CreditReservation reservation = reserve(event, true);
        if (reservation == null) return;

        RouletteGame game = RouletteGameManager.getGame(event.getChannel());
        if (game == null) {
            event.replyEmbeds(error("There is no game in-progress in this channel!")).setEphemeral(true).queue();
            return;
        }

        RoulettePlayerGame player = game.getOrCreatePlayer(event.getMember(), reservation);

        boolean additionalBetSuccess = player.startBet((int) reservation.getReserved());
        if (!additionalBetSuccess) {
            player.resendGui(msg -> event.reply(msg).setEphemeral(true));
            return;
        }

        RoulettePlayerGui gui = new RoulettePlayerGui(dcf, msg -> event.reply(msg).setEphemeral(true), player);
        gui.addPage(new RoulettePlayerMainPage(gui));
        player.setGui(gui);
        gui.send();
    }


    @Override
    public SubcommandData getData() {
        SubcommandData data = new SubcommandData("bet", "Bet credits and/or join an ongoing roulette game in this channel");
        addOptionAmount(data);
        return data;

    }

    @Override
    public int getInitialBetLimit() {
        return ONE_HAND_MAX_BET;
    }
}
