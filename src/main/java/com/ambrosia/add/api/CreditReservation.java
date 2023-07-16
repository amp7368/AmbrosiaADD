package com.ambrosia.add.api;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.game.GameBase;
import com.ambrosia.add.database.game.GameResultEntity;
import com.ambrosia.add.database.game.GameStorage;
import com.ambrosia.add.discord.util.CommandBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.Nullable;

public class CreditReservation {

    private final ClientEntity client;
    private final long reserved;
    private GameBase ongoingGame;
    private CreditReservationRejection rejection;
    private boolean notEnded = true;

    public CreditReservation(ClientEntity client, long reserved) {
        this.client = client;
        this.reserved = reserved;
    }

    public boolean notEnoughCredits() {
        return noPlayer() || getClientCredits() < getReserved();
    }

    public boolean noPlayer() {
        return this.client == null;
    }

    public boolean alreadyPlaying() {
        return this.ongoingGame != null;
    }

    public boolean isRejected() {
        return this.rejection != null;
    }

    public CreditReservation setRejected(CreditReservationRejection rejection) {
        this.rejection = rejection;
        return this;
    }

    public ClientEntity getClient() {
        return this.client;
    }

    public long getReserved() {
        return reserved;
    }

    public long getClientCredits() {
        return this.client == null ? 0 : client.credits;
    }


    public GameBase getOngoingGame() {
        return this.ongoingGame;
    }

    public CreditReservation setOngoingGame(@Nullable GameBase ongoingGame) {
        this.ongoingGame = ongoingGame;
        return this;
    }

    public void release(GameResultEntity result) {
        if (this.notEnded) {
            notEnded = false;
            GameStorage.get().endGame(this, result);
        }
    }

    public boolean checkError(SlashCommandInteractionEvent event) {
        if (this.noPlayer()) {
            CommandBuilder.instance.errorRegisterWithStaff(event);
            return true;
        }
        if (this.alreadyPlaying()) {
            event.reply("Please finish your previous game before starting a new one.").queue();
            return true;
        }
        if (this.notEnoughCredits()) {
            long difference = this.getReserved() - this.getClientCredits();
            event.reply("Sorry, you need " + difference + " more " + "credit" + (difference == 1 ? "." : "s.")).setEphemeral(true)
                .queue();
            return true;
        }
        return false;
    }
}
