package com.github.AndrewAlbizati.command;

import com.ambrosia.add.api.AmbrosiaAPI;
import com.ambrosia.add.api.CreditReservation;
import com.ambrosia.add.discord.util.BaseCommand;
import com.ambrosia.add.discord.util.Emeralds;
import com.github.AndrewAlbizati.game.BlackjackGame;
import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.util.TimeMillis;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

/**
 * Handles when a user executes the /blackjack command
 */
public class BlackjackCommand extends BaseCommand {


    @Override
    public SlashCommandData getData() {
        SlashCommandData command = Commands.slash("blackjack", "Plays a game of Blackjack with the credits you bet");
        addOptionAmount(command);
        return command;
    }

    @Override
    public void onCheckedCommand(SlashCommandInteractionEvent event) {
        User user = event.getUser();

        Integer bet = findOptionAmount(event);
        if (bet == null) return;
        if (isBetTooMuch(bet)) {
            event.reply("24 le limit").setEphemeral(true).queue();
            return;
        }
        // try to reserve with double-down
        CreditReservation reservation = AmbrosiaAPI.get().reserve(user.getIdLong(), bet);
        if (reservation.noPlayer()) {
            this.errorRegisterWithStaff(event);
            return;
        }
        if (reservation.alreadyPlaying()) {
            event.reply("Please finish your previous game before starting a new one.").queue();
            return;
        }
        if (reservation.notEnoughCredits()) {
            long difference = reservation.getReserved() - reservation.getClientCredits();
            event.reply("Sorry, you need " + difference + " more " + "credit" + (difference == 1 ? "." : "s.")).queue();
            return;
        }
        BlackjackGame game = new BlackjackGame(bet, reservation);

        DCFGui gui = new DCFGui(dcf, event::reply) {
            @Override
            public long getMillisToOld() {
                return TimeMillis.minToMillis(30);
            }
        };
        gui.addPage(new BlackjackGameGui(gui, user, game)).send();
    }

    private boolean isBetTooMuch(Integer bet) {
        return bet > Emeralds.leToEmeralds(24);
    }
}
