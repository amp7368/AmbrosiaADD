package com.ambrosia.add.discord.active.account;

import com.ambrosia.add.discord.active.base.ActiveRequestGui;

public class ActiveRequestAccountGui extends ActiveRequestGui<ActiveRequestAccount> {

    public ActiveRequestAccountGui(long message, ActiveRequestAccount data) {
        super(message, data);
    }

    @Override
    protected String description() {
        return "todo";
    }

    @Override
    protected String title() {
        return "Create account";
    }
}
