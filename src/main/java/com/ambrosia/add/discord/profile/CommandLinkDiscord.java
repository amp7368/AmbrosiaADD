package com.ambrosia.add.discord.profile;

import com.ambrosia.add.database.client.ClientDiscordDetails;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.discord.log.DiscordLog;
import com.ambrosia.add.discord.util.CommandBuilder;
import discord.util.dcf.slash.DCFSlashSubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public class CommandLinkDiscord extends DCFSlashSubCommand implements CommandBuilder {

    public static final String DISCORD_OPTION = "discord";

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        if (this.isBadPermission(event)) return;
        ClientEntity client = this.findClient(event);
        if (client == null) return;
        Member member = this.findOption(event, DISCORD_OPTION, OptionMapping::getAsMember);
        if (member == null) return;
        client.setDiscord(new ClientDiscordDetails(member));
        member.getUser().openPrivateChannel().queue(this::sendRegisteredMessage);
        if (client.trySave()) {
            event.replyEmbeds(this.embedClientProfile(client)).queue();
            DiscordLog.log().modifyDiscord(client, event.getUser());
        } else event.replyEmbeds(this.error("Discord was already assigned")).queue();
    }

    private void sendRegisteredMessage(PrivateChannel channel) {
        SelfUser self = dcf.jda().getSelfUser();
        User user = channel.getUser();
        if (user == null) return;
        MessageCreateBuilder message = new MessageCreateBuilder().setComponents(
                ActionRow.of(Button.link("https://discord.gg/tEAy2dGXWF", "Ambrosia Discord Server")))
            .setEmbeds(new EmbedBuilder().setDescription("""
                    Thank you for registering with Ambrosia Casino.
                                
                    This message is to instruct you on how to operate the Ambrosia ADD Bot. If you believe you have gotten this message in error, please message @Tealy#7401.
                                
                    All commands explained here can only be performed in the Ambrosia Discord server, within the Ambrosia Casino channel category.
                                
                    In order to check your profile, do /profile. This will allow you to monitor your credits. Keep in mind it may take a moment for newly cashed in credits to be applied to your account, as it must be entered into our database by an Ambrosia Casino employee.
                                
                    In order to begin a game of blackjack, do /blackjack. You must also enter in your bet using LE/EB/E denominations. You can bet up to a maximum of 16 LE per hand.
                                
                    In order to view the rules and more information of how the bot works, and get the links to view our source code, do /help blackjack.
                                
                    Best of luck!
                    """).setTitle("Ambrosia Casino").setAuthor(user.getAsTag(), null, channel.getUser().getAvatarUrl())
                .setThumbnail(self.getAvatarUrl())
                .build());
        channel.sendMessage(message.build()).queue();
    }

    @Override
    public SubcommandData getData() {
        SubcommandData command = new SubcommandData("discord", "Link a client's profile with their discord account");
        addOptionProfileName(command);
        return command.addOption(OptionType.USER, DISCORD_OPTION, "The discord account to associate", true);
    }
}
