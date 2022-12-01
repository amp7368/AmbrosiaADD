package com.ambrosia.add.discord.profile;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.discord.util.CommandBuilder;
import lib.DCFSlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.Nullable;

public class ViewProfileCommand extends DCFSlashCommand implements CommandBuilder {


    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        if (!this.isBadPermission(event)) return;
        @Nullable String clientName = getOptionProfileName(event);
        if (clientName == null) return;
        ClientEntity client = this.findClient(event, clientName);
        if (client == null) return;

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(client.displayName);
        String avatar;
        if (client.discord != null) {
            User clientUser = event.getJDA().getUserById(client.discord);
            avatar = clientUser == null ? null : clientUser.getEffectiveAvatarUrl();
        } else {
            avatar = null;
        }
        embed.setAuthor(client.displayName, null, avatar);
        embed.setFooter("footer here", "https://cdn.discordapp.com/icons/923749890104885271/a_52da37c184005a14d15538cb62271b9b.webp");
        embed.setTimestamp(client.dateCreated.toInstant());
        embed.addField("Credits", String.valueOf(client.credits), true);
        embed.setThumbnail("https://cdn.discordapp.com/icons/923749890104885271/a_52da37c184005a14d15538cb62271b9b.webp");
        embed.setDescription("idk what else to put here");
        event.replyEmbeds(embed.build()).queue();
    }

    @Override
    public CommandData getData() {
        SlashCommandData command = Commands.slash("profile", "View a client's profile");
        addOptionProfileName(command);
        return command.setDefaultPermissions(DefaultMemberPermissions.DISABLED).setGuildOnly(true);
    }
}
