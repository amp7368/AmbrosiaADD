package com.ambrosia.add.database.game;

import com.ambrosia.add.api.CreditReservation;
import com.ambrosia.add.database.client.ClientEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public abstract class GameBase {

    protected final CreditReservation creditReservation;
    private final List<Consumer<GameBase>> finishHooks = new ArrayList<>();
    private MessageChannel channel;

    public GameBase(CreditReservation creditReservation) {
        this.creditReservation = creditReservation;
    }

    public void start() {
        this.creditReservation.setOngoingGame(this);
        GameStorage.get().startGame(this);
    }


    public ClientEntity getClient() {
        return this.creditReservation.getClient();
    }


    public MessageChannel getChannel() {
        return this.channel;
    }

    public void setChannel(MessageChannel message) {
        this.channel = message;
    }

    public abstract String getName();

    public void addFinishHook(Consumer<GameBase> finishHook) {
        this.finishHooks.add(finishHook);
    }

    protected void end() {
        this.finishHooks.forEach(hook -> hook.accept(this));
    }
}
