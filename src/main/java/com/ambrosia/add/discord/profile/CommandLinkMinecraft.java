package com.ambrosia.add.discord.profile;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientMinecraftDetails;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.discord.util.CommandBuilder;
import io.ebean.DB;
import lib.DCFSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandLinkMinecraft extends DCFSlashCommand implements CommandBuilder {

    private static final String OPTION_MINECRAFT = "minecraft";

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        if (!this.isBadPermission(event)) return;
        ClientEntity client = this.findClient(event);
        if (client == null) return;
        String username = this.findOption(event, OPTION_MINECRAFT, OptionMapping::getAsString);
        ClientMinecraftDetails minecraft = ClientMinecraftDetails.fromUsername(username);
        if (minecraft == null) {
            event.replyEmbeds(error(String.format("Could not find %s's minecraft account", username))).queue();
            return;
        }
        client.setMinecraft(minecraft);
        client.save();
        event.replyEmbeds(this.embedClientProfile(client)).queue();

    }

    @Override
    public CommandData getData() {
        SlashCommandData command = Commands.slash("link_minecraft", "Link a client's profile with their discord account");
        addOptionProfileName(command);
        command.addOption(OptionType.STRING, OPTION_MINECRAFT, "The minecraft account to associate", true);
        return command.setDefaultPermissions(DefaultMemberPermissions.DISABLED).setGuildOnly(true);
    }
}
