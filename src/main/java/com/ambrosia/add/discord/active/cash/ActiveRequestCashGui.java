package com.ambrosia.add.discord.active.cash;

import com.ambrosia.add.discord.active.base.ActiveRequestGui;
import com.ambrosia.add.discord.util.Emeralds;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class ActiveRequestCashGui extends ActiveRequestGui<ActiveRequestCash> {


    public ActiveRequestCashGui(long message, ActiveRequestCash activeRequestCash) {
        super(message, activeRequestCash);
    }

    @Override
    protected List<Field> fields() {
        return List.of();
    }

    @Override
    protected String description() {
        return "";
    }

    @Override
    protected void onApprove() throws Exception {
        data.onApprove();
    }

    @Override
    protected String title() {
        return data.transactionType().displayName() + " " + Emeralds.message(Math.abs(data.getAmount()), Integer.MAX_VALUE, true);
    }

}
