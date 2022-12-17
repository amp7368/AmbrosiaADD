package com.ambrosia.add.discord.commands.player.help;

import discord.util.dcf.gui.base.gui.DCFGui;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.jetbrains.annotations.NotNull;

public class HelpHomePage extends HelpGuiPage {

    public HelpHomePage(DCFGui dcfGui) {
        super(dcfGui);
    }

    @Override
    protected MessageEmbed makeEmbed(EmbedBuilder eb) {
        eb.addField("Register",
            "To create a profile, use /request account [IGN] (display_name) with your Minecraft username, and optionally an account "
                + "nickname", false);
        eb.addField("Profile",
            "In order to check your profile, use /profile. This will allow you to monitor your credits. Keep in mind that it may "
                + "take a moment for newly cashed in credits to be applied to your account, as it must be entered into our database "
                + "by an Ambrosia Casino employee.", false);
        eb.addField("Deposit/Withdraw",
            "If you would like to deposit more credits into your account, or withdraw them back into emeralds, you can do so by "
                + "creating a request with the respective /request deposit and /request withdraw commands on our discord. You will "
                + "be notified once our Casino-Managers are available to take on your request and trade you in-game.", false);
        eb.addField("Note",
            "Please ensure you are trading with a proper Casino Manager before depositing/withdrawing your credits. If you are "
                + "unsure, you can ask for verification in the Ambrosia Discord.", false);
        return eb.build();
    }

    @NotNull
    protected ActionRow pageActionRow() {
        return ActionRow.of(WIKI, GITHUB);
    }

    @Override
    protected String getTitle() {
        return "Ambrosia Casino";
    }


}
