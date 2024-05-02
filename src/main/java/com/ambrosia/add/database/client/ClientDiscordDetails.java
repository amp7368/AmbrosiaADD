package com.ambrosia.add.database.client;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

@Embeddable()
public class ClientDiscordDetails {

    @Column(unique = true)
    public Long id;
    @Column
    public String avatarUrl;
    @Column
    public String username;
    @Column
    public String guildName;
    @Column
    public String discriminator;

    public ClientDiscordDetails(Member member) {
        this.id = member.getIdLong();
        this.avatarUrl = member.getEffectiveAvatarUrl();
        this.guildName = member.getEffectiveName();
        User user = member.getUser();
        this.username = user.getName();
        this.discriminator = null;
    }

    public String fullName() {
        return this.username;
    }
}
