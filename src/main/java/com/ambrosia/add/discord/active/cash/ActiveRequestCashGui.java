package com.ambrosia.add.discord.active.cash;

import com.ambrosia.add.discord.active.base.ActiveRequestGui;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class ActiveRequestCashGui extends ActiveRequestGui<ActiveRequestCash> {


    public ActiveRequestCashGui(long message, ActiveRequestCash activeRequestCash) {
        super(message, activeRequestCash);
    }

    @Override
    protected void updateSender() {
    }

    @Override
    protected void onComplete() {

    }

    @Override
    protected List<Field> fields() {
        return List.of();
    }

    @Override
    protected String description() {
        return null;
    }

    @Override
    protected String title() {
        return null;
    }

}
