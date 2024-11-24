package com.ambrosia.add.discord.misc;

import com.ambrosia.add.database.client.ClientDiscordDetails;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.query.QClientEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import me.xdrop.fuzzywuzzy.FuzzySearch;

public class ClientSearch {

    public static List<ClientEntity> autoComplete(String match) {
        List<ClientName> byName = new ArrayList<>();

        List<ClientEntity> clients = new QClientEntity().findList();
        for (ClientEntity client : clients) {
            String displayName = client.getDisplayName();
            String minecraft = client.minecraft == null ? null : client.minecraft.name;
            ClientDiscordDetails discordDetails = client.discord;
            String discord = discordDetails == null ? null : discordDetails.username;
            byName.add(new ClientName(client, displayName, discord, minecraft));
        }

        byName.forEach(c -> c.match(match));

        byName.sort(Comparator.comparing(ClientName::score).reversed());
        return byName.stream().map(ClientName::getClient).toList();
    }

    private static class ClientName {

        private final List<String> names;
        private final ClientEntity client;
        private int score;

        private ClientName(ClientEntity client, String... names) {
            this.client = client;
            this.names = Arrays.stream(names).filter(Objects::nonNull).toList();
        }

        protected void match(String match) {
            String matchLower = match.toLowerCase();
            for (String name : names) {
                int score = FuzzySearch.partialRatio(matchLower, name.toLowerCase());
                if (score > this.score)
                    this.score = score;
            }
        }

        protected int score() {
            return this.score;
        }

        public ClientEntity getClient() {
            return this.client;
        }
    }
}
