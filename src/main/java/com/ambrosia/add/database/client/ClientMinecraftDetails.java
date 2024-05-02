package com.ambrosia.add.database.client;

import apple.utilities.fileio.serializing.FileIOJoined;
import apple.utilities.threading.service.queue.TaskHandlerQueue;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.jetbrains.annotations.Nullable;

@Embeddable
public class ClientMinecraftDetails {

    private static final String MOJANG_API = "https://api.mojang.com/users/profiles/minecraft/";
    private static final TaskHandlerQueue rateLimited = new TaskHandlerQueue(100, 10 * 60 * 10000, 5000);

    @Column(unique = true)
    public String name;

    @Column(unique = true)
    public String uuid;

    @Nullable
    public static ClientMinecraftDetails fromUsername(String username) {
        return rateLimited.taskCreator().accept(() -> loadUrl(username)).complete();
    }

    @Nullable
    private static ClientMinecraftDetails loadUrl(String username) {
        try {
            InputStream urlInput = new URL(MOJANG_API + username).openConnection().getInputStream();
            return FileIOJoined.get().loadJson(urlInput, ClientMinecraftDetails.class, null);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ClientMinecraftDetails other)
            return this.uuid.equals(other.uuid) && this.name.equals(other.name);
        return false;
    }

    public String skinUrl() {
        return "https://mc-heads.net/head/" + this.uuid;
    }
}
