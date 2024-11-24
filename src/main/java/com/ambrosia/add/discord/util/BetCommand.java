package com.ambrosia.add.discord.util;

import com.ambrosia.add.api.AmbrosiaAPI;
import com.ambrosia.add.api.CreditReservation;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface BetCommand extends CommandBuilder {

    default CreditReservation reserve(SlashCommandInteractionEvent event, boolean allowAlreadyPlaying) {
        Integer bet = findOptionAmount(event);
        if (bet == null) return null;
        if (bet > getInitialBetLimit()) {
            String betLimitMessage = Emeralds.message(getInitialBetLimit(), 2, true);
            event.reply("%s per bet limit".formatted(betLimitMessage)).setEphemeral(true).queue();
            return null;
        }
        CreditReservation reservation = AmbrosiaAPI.get().reserve(event.getUser().getIdLong(), bet, allowAlreadyPlaying);
        if (reservation.checkError(event)) return null;
        return reservation;
    }

    int getInitialBetLimit();

}
