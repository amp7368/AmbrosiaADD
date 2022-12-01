package com.ambrosia.add.discord.profile;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientMinecraftDetails;
import com.ambrosia.add.discord.util.CommandBuilder;
import lib.slash.DCFSlashSubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandLinkMinecraft extends DCFSlashSubCommand implements CommandBuilder {

    private static final String OPTION_MINECRAFT = "minecraft";

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        if (this.isBadPermission(event)) return;
        ClientEntity client = this.findClient(event);
        if (client == null) return;
        String username = this.findOption(event, OPTION_MINECRAFT, OptionMapping::getAsString);
        if (username == null) return;
        ClientMinecraftDetails minecraft = ClientMinecraftDetails.fromUsername(username);
        if (minecraft == null) {
            event.replyEmbeds(error(String.format("Could not find %s's minecraft account", username))).queue();
            return;
        }
        client.setMinecraft(minecraft);
        if (client.trySave()) event.replyEmbeds(this.embedClientProfile(client)).queue();
        else event.replyEmbeds(this.error("Minecraft was already assigned")).queue();

    }

    @Override
    public SubcommandData getData() {
        SubcommandData command = new SubcommandData("minecraft", "Link a client's profile with their minecraft account");
        addOptionProfileName(command);
        return command.addOption(OptionType.STRING, OPTION_MINECRAFT, "The minecraft account to associate", true);
    }
}
