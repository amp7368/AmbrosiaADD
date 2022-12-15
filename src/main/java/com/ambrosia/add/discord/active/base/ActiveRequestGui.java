package com.ambrosia.add.discord.active.base;

import apple.utilities.util.ArrayUtils;
import apple.utilities.util.Pretty;
import com.ambrosia.add.discord.active.ActiveRequestDatabase;
import com.ambrosia.add.discord.active.account.UpdateAccountException;
import discord.util.dcf.gui.stored.DCFStoredGui;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

public abstract class ActiveRequestGui<Data extends ActiveRequest<?>> extends DCFStoredGui<Data> {

    private static final String BUTTON_DENY_ID = "deny";
    private static final String BUTTON_CLAIM_ID = "claim";
    private static final String BUTTON_COMPLETE_ID = "complete";
    private static final String BUTTON_BACK_ID = "back";
    private static final Button BUTTON_BACK = Button.secondary(BUTTON_BACK_ID, "Back");
    private static final Button BUTTON_COMPLETE = Button.success(BUTTON_COMPLETE_ID, "Complete");
    private static final Button BUTTON_DENY = Button.danger(BUTTON_DENY_ID, "Deny");
    private static final Button BUTTON_CLAIM = Button.primary(BUTTON_CLAIM_ID, "Claim");
    private String error = null;

    public ActiveRequestGui(long message, Data data) {
        super(message, data);
        this.registerButton(BUTTON_DENY_ID, this::deny);
        this.registerButton(BUTTON_CLAIM_ID, this::claim);
        this.registerButton(BUTTON_COMPLETE_ID, this::complete);
        this.registerButton(BUTTON_BACK_ID, this::unClaim);
    }

    private void unClaim(ButtonInteractionEvent event) {
        this.data.stage = ActiveRequestStage.UNCLAIMED;
        this.data.sender.setEndorser(event.getUser().getAsTag());
        this.updateSender();
    }

    private void deny(ButtonInteractionEvent event) {
        this.data.stage = ActiveRequestStage.DENIED;
        this.data.sender.setEndorser(event.getUser().getAsTag());
        this.remove();
        this.updateSender();
    }

    private void claim(ButtonInteractionEvent event) {
        this.data.stage = ActiveRequestStage.CLAIMED;
        this.data.sender.setEndorser(event.getUser().getAsTag());
        this.updateSender();
    }

    private void complete(ButtonInteractionEvent event) {
        this.data.stage = ActiveRequestStage.COMPLETED;
        this.data.sender.setEndorser(event.getUser().getAsTag());
        try {
            this.onComplete();
        } catch (UpdateAccountException e) {
            this.data.stage = ActiveRequestStage.ERROR;
            this.error = e.getMessage();
        }
        this.remove();
        this.updateSender();
    }

    protected abstract void updateSender();

    protected abstract void onComplete() throws UpdateAccountException;

    @Override
    protected MessageCreateData makeMessage() {
        return makeMessage("");
    }

    public MessageCreateData makeClientMessage(String... extraDescription) {
        return MessageCreateBuilder.from(this.makeMessage(extraDescription)).setComponents().build();
    }

    protected MessageCreateData makeMessage(String... extraDescription) {
        MessageCreateBuilder message = new MessageCreateBuilder();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(String.format("%s - (%s)", title(), stageName()));
        embed.setColor(this.data.stage.getColor());
        data.sender.author(embed);
        String description = this.generateDescription(ArrayUtils.combine(extraDescription, error, String[]::new));
        embed.setDescription(description);
        embed.addBlankField(false);
        this.fields().forEach(embed::addField);
        message.setEmbeds(embed.build());
        List<Button> components = switch (data.stage) {
            case ERROR, DENIED, COMPLETED -> List.of();
            case CLAIMED -> List.of(BUTTON_BACK, BUTTON_COMPLETE);
            case CREATED, UNCLAIMED -> this.getInitialComponents();
        };
        if (components.isEmpty()) message.setComponents();
        else message.setComponents(ActionRow.of(components));
        return message.build();
    }

    private String generateDescription(String... extra) {
        StringBuilder description = new StringBuilder(description());
        for (String next : extra) {
            if (next == null || next.isBlank()) continue;
            if (!description.toString().isBlank()) description.append("\n\n");
            description.append(next);
        }
        return description.toString();
    }

    @NotNull
    private String stageName() {
        return Pretty.spaceEnumWords(data.stage.name());
    }

    private List<Button> getInitialComponents() {
        if (this.hasClaimButton()) return List.of(BUTTON_DENY, BUTTON_CLAIM);
        return List.of(BUTTON_DENY, BUTTON_COMPLETE);
    }

    protected boolean hasClaimButton() {
        return true;
    }

    protected abstract List<Field> fields();

    protected abstract String description();

    protected abstract String title();

    @Override
    public void save() {
        ActiveRequestDatabase.save(this.serialize());
    }

    @Override
    public void remove() {
        ActiveRequestDatabase.remove(this.serialize());
    }
}
