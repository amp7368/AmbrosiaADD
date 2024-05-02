package com.ambrosia.add.discord.commands.player.request;

import com.ambrosia.add.discord.DiscordModule;
import com.ambrosia.add.discord.active.ActiveRequestDatabase;
import com.ambrosia.add.discord.active.account.ActiveRequestAccount;
import com.ambrosia.add.discord.active.account.ActiveRequestAccountGui;
import com.ambrosia.add.discord.util.BaseSubCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandRequestAccount extends BaseSubCommand {

    private static final String OPTION_MINECRAFT = "minecraft";
    private static final String OPTION_DISPLAY_NAME = "display_name";

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if (member == null) return;
        String minecraft = findOption(event, OPTION_MINECRAFT, OptionMapping::getAsString);
        String displayName = findOption(event, OPTION_DISPLAY_NAME, OptionMapping::getAsString, false);
        ActiveRequestAccount request;
        try {
            request = new ActiveRequestAccount(member, minecraft, displayName);
        } catch (Exception e) {
            event.replyEmbeds(error(e.getMessage())).queue();
            DiscordModule.get().logger().error("", e);
            return;
        }

        ActiveRequestAccountGui gui = request.create();
        event.reply(gui.makeClientMessage()).queue();
        gui.send(ActiveRequestDatabase::sendRequest);
    }

    @Override
    public SubcommandData getData() {
        SubcommandData command = new SubcommandData("account", "Request to create/update an account");
        command.addOption(OptionType.STRING, OPTION_MINECRAFT, "Your minecraft in-game name", true);
        command.addOption(OptionType.STRING, OPTION_DISPLAY_NAME, "Your profile display name");
        return command;
    }
}
