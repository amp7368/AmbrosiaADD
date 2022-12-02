package com.ambrosia.add.database.client;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

@Embeddable()
public class ClientDiscordDetails {

    @EmbeddedId
    @Column(unique = true)
    public Long discordId;
    @Column
    public String avatarUrl;
    @Column
    public String username;
    @Column
    public String guildName;
    @Column
    public String discriminator;

    public ClientDiscordDetails(Member member) {
        this.discordId = member.getIdLong();
        this.avatarUrl = member.getEffectiveAvatarUrl();
        this.guildName = member.getEffectiveName();
        User user = member.getUser();
        this.username = user.getName();
        this.discriminator = user.getDiscriminator();
    }

    public String fullName() {
        return this.username + "#" + this.discriminator;
    }
}
