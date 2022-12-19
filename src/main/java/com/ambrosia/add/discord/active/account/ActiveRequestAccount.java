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
import org.jetbrains.annotations.Nullable;

public class ActiveRequestAccount extends ActiveRequest<ActiveRequestAccountGui> {

    public ClientEntity original;
    public ClientEntity updated;

    public ActiveRequestAccount() {
        super(ActiveRequestType.ACCOUNT.getTypeId(), null);
    }

    public ActiveRequestAccount(Member discord, String minecraft, String displayName)
        throws UpdateAccountException {
        super(ActiveRequestType.ACCOUNT.getTypeId(), new ActiveRequestSender(discord, null));
        ClientDiscordDetails discordDetails = new ClientDiscordDetails(discord);
        ClientMinecraftDetails minecraftDetails = ClientMinecraftDetails.fromUsername(minecraft);
        if (minecraftDetails == null) throw new UpdateAccountException(String.format("Minecraft account: '%s' not found", minecraft));
        this.original = ClientStorage.get().findByDiscord(discord.getIdLong());
        if(displayName == null) displayName = minecraft;
        if (this.original == null) this.original = ClientStorage.get().createClient(displayName, discordDetails);
        sender.setClient(original);
        this.updated = ClientStorage.get().findByUUID(original.uuid);
        if (updated == null) throw new IllegalStateException("Client " + original.uuid + " does not exist!");
        this.updated.minecraft = minecraftDetails;
        if (displayName != null) this.updated.displayName = displayName;
        if (displayFields().isEmpty()) throw new UpdateAccountException("No updates were specified so no changes were made");
    }


    @Override
    public ActiveRequestAccountGui load() {
        return new ActiveRequestAccountGui(messageId, this);
    }

    public void onComplete() throws UpdateAccountException {
        ClientEntity newVersion = ClientStorage.get().findByUUID(updated.uuid);
        if (newVersion == null) throw new UpdateAccountException("Client no longer exists");
        newVersion.minecraft = updateField(newVersion, client -> client.minecraft);
        newVersion.displayName = updateField(newVersion, client -> client.displayName);
        ClientStorage.get().updateClient(newVersion);
        newVersion.trySave();
    }

    public <T> T updateField(ClientEntity newVersion, Function<ClientEntity, T> extract) {
        boolean isAnUpdate = Objects.equals(extract.apply(original), extract.apply(updated));
        return extract.apply(isAnUpdate ? newVersion : updated);
    }

    public List<Field> displayFields() {
        List<Field> fields = new ArrayList<>();
        fields.add(checkEqual((client) -> client.displayName, Objects::toString, "Profile DisplayName"));
        fields.add(checkEqual((client) -> client.minecraft, mc -> mc.name, "Minecraft"));
        fields.removeIf(Objects::isNull);
        return fields;
    }

    @Nullable
    private <T> Field checkEqual(Function<ClientEntity, T> extractKey, Function<T, String> toString, String title) {
        T original = extractKey.apply(this.original);
        T updated = extractKey.apply(this.updated);
        if (Objects.equals(original, updated)) return null;
        String originalMsg = original == null ? "None" : toString.apply(original);
        String updatedMsg = updated == null ? "None" : toString.apply(updated);
        return new Field(title, String.format("%s :arrow_right: %s", originalMsg, updatedMsg), false);
    }


}
