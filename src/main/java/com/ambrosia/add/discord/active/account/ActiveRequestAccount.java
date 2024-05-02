package com.ambrosia.add.discord.active.account;

import com.ambrosia.add.database.client.ClientDiscordDetails;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientMinecraftDetails;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.discord.active.ActiveRequestType;
import com.ambrosia.add.discord.active.base.ActiveRequest;
import com.ambrosia.add.discord.active.base.ActiveRequestSender;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActiveRequestAccount extends ActiveRequest<ActiveRequestAccountGui> {


    private ClientMinecraftDetails minecraft;
    private String displayName;
    private long clientId;
    private transient ClientEntity client;

    public ActiveRequestAccount() {
        super(ActiveRequestType.ACCOUNT.getTypeId(), null);
    }

    public ActiveRequestAccount(Member discord, String minecraft, String displayName)
        throws UpdateAccountException {
        super(ActiveRequestType.ACCOUNT.getTypeId(), new ActiveRequestSender(discord, null));
        ClientDiscordDetails discordDetails = new ClientDiscordDetails(discord);
        ClientMinecraftDetails minecraftDetails = ClientMinecraftDetails.fromUsername(minecraft);
        if (minecraftDetails == null) throw new UpdateAccountException(String.format("Minecraft account: '%s' not found", minecraft));
        if (displayName == null) displayName = minecraft;
        ClientEntity original = ClientStorage.get().findByDiscord(discord.getIdLong());
        if (original == null) original = ClientStorage.get().createClient(displayName, discordDetails);
        this.clientId = original.id;
        sender.setClient(original);
        this.minecraft = minecraftDetails;
        if (displayName != null) this.displayName = displayName;
        if (displayFields().isEmpty()) throw new UpdateAccountException("No updates were specified so no changes were made");
    }


    @Override
    public ActiveRequestAccountGui load() {
        return new ActiveRequestAccountGui(messageId, this);
    }

    public void onComplete() throws UpdateAccountException {
        ClientEntity client = getClient();
        if (minecraft != null)
            client.setMinecraft(minecraft);
        if (displayName != null)
            client.setDisplayName(displayName);
        client.save();
    }

    @NotNull
    private ClientEntity getClient() {
        if (this.client != null) return client;
        this.client = ClientStorage.get().findById(clientId);
        if (client == null) throw new IllegalStateException("Client no longer exists");
        return client;
    }

    public List<Field> displayFields() {
        List<Field> fields = new ArrayList<>();
        fields.add(checkEqual(getClient().displayName, displayName, Objects::toString, "Profile DisplayName"));
        fields.add(checkEqual(getClient().minecraft, minecraft, mc -> mc.name, "Minecraft"));
        fields.removeIf(Objects::isNull);
        return fields;
    }

    @Nullable
    private <T> Field checkEqual(T original, T updated, Function<T, String> toString, String title) {
        if (Objects.equals(original, updated)) return null;
        String originalMsg = original == null ? "None" : toString.apply(original);
        String updatedMsg = updated == null ? "None" : toString.apply(updated);
        return new Field(title, String.format("%s :arrow_right: %s", originalMsg, updatedMsg), false);
    }


}
