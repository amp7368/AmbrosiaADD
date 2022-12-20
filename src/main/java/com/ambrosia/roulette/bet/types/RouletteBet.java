package com.ambrosia.roulette.bet.types;

import com.ambrosia.roulette.bet.RouletteBetPart;
import java.util.List;

public abstract class RouletteBet {

    private final String typeId;
    protected transient RouletteBetType type;

    public RouletteBet(RouletteBetType type) {
        this.type = type;
        this.typeId = type.getTypeId();
    }

    public String displayName() {
        return this.type.displayName();
    }

    public double betMultiplier() {
        return this.type.betMultiplier();
    }

    public abstract List<RouletteBetPart> actions();

    public abstract List<RouletteBetPart> partList();

    public abstract boolean isComplete();

    public String getTypeId() {
        return typeId;
    }

    public abstract boolean isWinningSpace(int roll);

}
