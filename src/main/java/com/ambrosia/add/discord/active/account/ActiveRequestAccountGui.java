package com.ambrosia.add.discord.active.account;

import com.ambrosia.add.discord.active.base.ActiveRequestGui;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class ActiveRequestAccountGui extends ActiveRequestGui<ActiveRequestAccount> {

    public ActiveRequestAccountGui(long message, ActiveRequestAccount data) {
        super(message, data);
    }

    @Override
    protected void updateSender() {
        String updateMessage = switch (this.data.stage) {
            case DENIED -> "**%s** has denied this request";
            case CLAIMED -> "**%s** has seen your request";
            case COMPLETED -> "**%s** has completed request";
            case UNCLAIMED -> "**%s** has stopped working on your request. Someone else will come along to complete it.";
            case ERROR -> "There was an error processing the request D: Message **%s**";
            case CREATED -> "";
        };
        updateMessage = String.format(updateMessage, data.sender.getEndorser());
        data.sender.sendDm(makeClientMessage(updateMessage));
    }


    @Override
    protected String description() {
        return "";
    }

    @Override
    protected List<Field> fields() {
        return data.displayFields();
    }

    @Override
    protected void onComplete() throws UpdateAccountException {
        this.data.onComplete();
    }

    @Override
    protected String title() {
        return "Update account";
    }
}
