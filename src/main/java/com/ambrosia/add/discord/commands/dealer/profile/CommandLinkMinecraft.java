package com.ambrosia.add.discord.commands.dealer.profile;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientMinecraftDetails;
import com.ambrosia.add.discord.log.DiscordLog;
import com.ambrosia.add.discord.util.BaseSubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandLinkMinecraft extends BaseSubCommand {

    private static final String OPTION_MINECRAFT = "minecraft";

    @Override
    public void onCheckedCommand(SlashCommandInteractionEvent event) {
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
        if (client.trySave()) {
            event.replyEmbeds(this.embedClientProfile(client)).queue();
            DiscordLog.log().modifyMinecraft(client, event.getUser());
        } else event.replyEmbeds(this.error("Minecraft was already assigned")).queue();

    }

    @Override
    public boolean isOnlyDealer() {
        return true;
    }

    @Override
    public SubcommandData getData() {
        SubcommandData command = new SubcommandData("minecraft", "Link a client's profile with their minecraft account");
        addOptionProfileName(command);
        return command.addOption(OptionType.STRING, OPTION_MINECRAFT, "The minecraft account to associate", true);
    }
}
