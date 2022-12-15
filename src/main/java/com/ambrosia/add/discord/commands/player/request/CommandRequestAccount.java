package com.ambrosia.add.discord.commands.player.request;

import com.ambrosia.add.discord.active.ActiveRequestDatabase;
import com.ambrosia.add.discord.active.account.ActiveRequestAccount;
import com.ambrosia.add.discord.util.BaseSubCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandRequestAccount extends BaseSubCommand {

    private static final String OPTION_MINECRAFT = "minecraft";

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if (member == null) return;
        String minecraft = findOption(event, OPTION_MINECRAFT, OptionMapping::getAsString);

        ActiveRequestAccount request = ActiveRequestAccount.create(member, minecraft);
        if (request == null) {
            event.replyEmbeds(error(String.format("Couldn't find minecraft account '%s'", minecraft))).queue();
            return;
        }
        request.create().send(event::reply);
    }

    @Override
    public SubcommandData getData() {
        SubcommandData command = new SubcommandData("account", "Request to create an account");
        command.addOption(OptionType.STRING, OPTION_MINECRAFT, "Your minecraft in-game name", true);
        return command;
    }
}
