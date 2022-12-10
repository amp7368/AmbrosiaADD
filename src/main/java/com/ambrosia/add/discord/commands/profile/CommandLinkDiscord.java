package com.ambrosia.add.discord.commands.profile;

import com.ambrosia.add.Ambrosia;
import com.ambrosia.add.database.client.ClientDiscordDetails;
import com.ambrosia.add.database.client.ClientEntity;
import com.ambrosia.add.discord.DiscordBot;
import com.ambrosia.add.discord.log.DiscordLog;
import com.ambrosia.add.discord.util.BaseSubCommand;
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
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public class CommandLinkDiscord extends BaseSubCommand {

    public static final String DISCORD_OPTION = "discord";

    @Override
    public void onCheckedCommand(SlashCommandInteractionEvent event) {
        ClientEntity client = this.findClient(event);
        if (client == null) return;
        Member member = this.findOption(event, DISCORD_OPTION, OptionMapping::getAsMember);
        if (member == null) return;
        client.setDiscord(new ClientDiscordDetails(member));
        sendRegistrationMessage(member);
        if (client.trySave()) {
            event.replyEmbeds(this.embedClientProfile(client)).queue();
            DiscordLog.log().modifyDiscord(client, event.getUser());
        } else event.replyEmbeds(this.error("Discord was already assigned")).queue();
    }

    @Override
    public boolean isOnlyDealer() {
        return true;
    }

    public static void sendRegistrationMessage(Member member) {
        member.getUser().openPrivateChannel().queue(CommandLinkDiscord::sendRegisteredMessage);
    }

    private static void sendRegisteredMessage(PrivateChannel channel) {
        SelfUser self = DiscordBot.dcf.jda().getSelfUser();
        User user = channel.getUser();
        if (user == null) return;
        MessageCreateBuilder message = new MessageCreateBuilder().setComponents(ActionRow.of(Ambrosia.inviteButton()))
            .setEmbeds(new EmbedBuilder().setDescription("""
                    Thank you for registering with Ambrosia Casino. The following message will instruct you on how to operate the Ambrosia ADD Bot. If you believe you have gotten this message in error, please message @Tealy#7401.
                    All commands listed below can only be performed in the Ambrosia Discord server, within the Ambrosia Casino channel category. If you have not yet joined, here's an invite link: https://discord.gg/tEAy2dGXWF
                                        
                    - In order to check your profile, use /profile. This will allow you to monitor your credits. Keep in mind that it may take a moment for newly cashed in credits to be applied to your account, as it must be entered into our database by an Ambrosia Casino employee.
                                        
                    - To begin a game of blackjack, type /blackjack. You will also be asked to specify your bet in LE/EB/E. You can wager up to a maximum of 24 LE per hand.
                                        
                    - In order to view the rules and more information on how the bot functions, aswell as to get the links to view our source code, do /help blackjack.
                    Thank you for registering with Ambrosia Casino, best of luck!
                    """).setTitle("Ambrosia Casino").setAuthor(user.getAsTag(), null, channel.getUser().getAvatarUrl())
                .setThumbnail(self.getAvatarUrl()).build());
        channel.sendMessage(message.build()).queue();
    }


    @Override
    public SubcommandData getData() {
        SubcommandData command = new SubcommandData("discord", "Link a client's profile with their discord account");
        addOptionProfileName(command);
        return command.addOption(OptionType.USER, DISCORD_OPTION, "The discord account to associate", true);
    }
}
