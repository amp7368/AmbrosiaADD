package com.ambrosia.add.discord.active.base;

import apple.utilities.util.Pretty;
import com.ambrosia.add.discord.active.ActiveRequestDatabase;
import discord.util.dcf.gui.stored.DCFStoredGui;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public abstract class ActiveRequestGui<Data extends ActiveRequest> extends DCFStoredGui {

    protected Data data;

    public ActiveRequestGui(long message, Data data) {
        super(message);
        this.data = data;
    }

    @Override
    protected MessageCreateData makeMessage() {
        MessageCreateBuilder message = new MessageCreateBuilder();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title());
        embed.setAuthor(Pretty.spaceEnumWords(data.stage.name()));
        embed.setDescription(this.description());
        message.setEmbeds(embed.build());
        List<Button> components = switch (data.stage) {
            case CREATED -> List.of(Button.danger("deny", "Deny"), Button.primary("claim", "Claim"));
        };
        message.setComponents(ActionRow.of(components));
        return message.build();
    }

    protected abstract String description();

    protected abstract String title();

    @Override
    public void save() {
        ActiveRequestDatabase.save(this.serialize());
    }

    public void remove() {
        ActiveRequestDatabase.remove(this.serialize());
    }

    protected ActiveRequest serialize() {
        return data;
    }
}
