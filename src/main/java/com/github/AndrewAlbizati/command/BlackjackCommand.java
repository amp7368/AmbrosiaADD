package com.github.AndrewAlbizati.command;

import com.ambrosia.add.api.CreditReservation;
import com.ambrosia.add.discord.util.BaseCommand;
import com.ambrosia.add.discord.util.BetCommand;
import com.ambrosia.add.discord.util.Emeralds;
import com.github.AndrewAlbizati.game.BlackjackGame;
import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.util.TimeMillis;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

/**
 * Handles when a user executes the /blackjack command
 */
public class BlackjackCommand extends BaseCommand implements BetCommand {


    @Override
    public SlashCommandData getData() {
        SlashCommandData command = Commands.slash("blackjack", "Plays a game of Blackjack with the credits you bet");
        addOptionAmount(command);
        return command;
    }

    @Override
    public void onCheckedCommand(SlashCommandInteractionEvent event) {
        CreditReservation reservation = reserve(event, false);
        if (reservation == null) return;

        BlackjackGame game = new BlackjackGame((int) reservation.getReserved(), reservation);
        game.setChannel(event.getMessageChannel());
        game.start();
        DCFGui gui = new DCFGui(dcf, event::reply) {
            @Override
            public long getMillisToOld() {
                return TimeMillis.minToMillis(30);
            }

        };
        gui.addPage(new BlackjackGameGui(gui, event.getUser(), game)).send();
    }

    @Override
    public int getInitialBetLimit() {
        return Emeralds.leToEmeralds(24);
    }
}
