package com.ambrosia.roulette.gif;

import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RouletteGifGui extends DCFGuiPage<DCFGui> {

    private String url;

    public RouletteGifGui(DCFGui gui, String url) {
        super(gui);
        this.url = url;
        registerButton("bad", this::delete);
    }

    private void delete(ButtonInteractionEvent event) {
        getParent().remove();
        event.editMessageEmbeds().setComponents().setContent(":white_check_mark:").queue();
    }
    @Override
    public boolean editOnInteraction() {
        return false;
    }

    @Override
    public void remove() {
    }

    @Override
    public MessageCreateData makeMessage() {
        MessageCreateBuilder message = new MessageCreateBuilder();
        EmbedBuilder eb = new EmbedBuilder().setImage(url);
        eb.setTitle("Hellos");
        message.setEmbeds(eb.build());
        message.setComponents(ActionRow.of(Button.danger("bad", "Do not touch")));
        return message.build();
    }
}
