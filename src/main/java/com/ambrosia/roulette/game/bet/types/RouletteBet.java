package com.ambrosia.roulette.game.bet.types;

import com.ambrosia.add.discord.util.Emeralds;
import com.ambrosia.roulette.game.player.RoulettePartialBet;
import com.ambrosia.roulette.game.player.RoulettePlayerGame;
import java.time.Instant;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public abstract class RouletteBet {

    public int betAmount;
    protected final transient RoulettePlayerGame player;
    protected final transient Instant timestamp = Instant.now();
    protected String typeId;
    protected transient RouletteBetType<?> type;

    public RouletteBet(RouletteBetType<?> type, RoulettePartialBet bet) {
        this.type = type;
        this.betAmount = bet.amount();
        this.player = bet.player();
        this.typeId = type.getTypeId();
    }

    public int getAmount() {
        return this.betAmount;
    }

    public Field toDiscordField() {
        return toDiscordField(true);
    }

    public Field toDiscordField(boolean bold) {
        String emeralds = Emeralds.message(getAmount(), bold);
        String title = "%s (%s)".formatted(getType().displayName(), emeralds);

        String description = "%s".formatted(shortDescription(bold));
        return new Field(title, description, false);
    }

    protected abstract String shortDescription(boolean bold);

    public RouletteBetType<?> getType() {
        return this.type;
    }

    public abstract boolean isWinningSpace(int roll);

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public RoulettePlayerGame getPlayer() {
        return player;
    }

    public int winAmount() {
        return (int) Math.ceil(this.betAmount * this.type.betMultiplier());
    }

    public abstract String toJson();
}
