package com.ambrosia.add.discord.active.base;

import com.ambrosia.add.discord.util.AmbrosiaColor;
import com.ambrosia.add.discord.util.AmbrosiaColor.AmbrosiaColorRequest;

public enum ActiveRequestStage {
    DENIED(AmbrosiaColor.BAD),
    CLAIMED(AmbrosiaColorRequest.CLAIMED),
    COMPLETED(AmbrosiaColor.SUCCESS),
    UNCLAIMED(AmbrosiaColorRequest.UNCLAIMED),
    ERROR(AmbrosiaColor.ERROR),
    CREATED(AmbrosiaColor.NORMAL);

    private final int color;

    ActiveRequestStage(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
