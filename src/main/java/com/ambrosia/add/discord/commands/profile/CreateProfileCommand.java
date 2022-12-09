package com.ambrosia.add.discord.commands.profile;

import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.database.client.ClientStorage;
import com.ambrosia.add.discord.log.DiscordLog;
import com.ambrosia.add.discord.util.BaseCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.Nullable;

public class CreateProfileCommand extends BaseCommand {

    @Override
    public void onCheckedCommand(SlashCommandInteractionEvent event) {
        @Nullable String clientName = findOptionProfileName(event);
        if (clientName == null) return;
        Member discord = this.findOption(event, CommandLinkDiscord.DISCORD_OPTION, OptionMapping::getAsMember, false);

        long conductorId = event.getUser().getIdLong();
        ClientEntity client = ClientStorage.get().createClient(conductorId, clientName, discord);
        if (client == null) {
            event.replyEmbeds(this.error(String.format("'%s' is already a user", clientName))).queue();
            return;
        }
        event.replyEmbeds(this.success(String.format("Successfully created %s", client.displayName))).queue();
        DiscordLog.log().createAccount(client, event.getUser());
    }


    public SlashCommandData getData() {
        SlashCommandData command = Commands.slash("create", "Create a profile for a customer");
        addOptionProfileName(command);
        command.addOption(OptionType.USER, CommandLinkDiscord.DISCORD_OPTION, "The discord account to associate");
        return command.setDefaultPermissions(DefaultMemberPermissions.DISABLED).setGuildOnly(true);
    }

    @Override
    public boolean isOnlyDealer() {
        return true;
    }
}
