package com.ambrosia.add.discord.commands.player.help;

import discord.util.dcf.gui.base.gui.DCFGui;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;

public class HelpCommandsPage extends HelpGuiPage {


    public HelpCommandsPage(DCFGui gui) {
        super(gui);
    }

    @Override
    protected MessageEmbed makeEmbed(EmbedBuilder eb) {
        eb.addField("/request account [IGN]", "Request to create a profile with your Minecraft in-game name", false);
        eb.addField("/request withdraw [emeralds]", "Request to cashout emeralds from your account", false);
        eb.addField("/request deposit [emeralds]", "Request to deposit emeralds into your account", false);
        eb.addField("/profile", "Display your profile", false);
        eb.addField("/trade [profile_name] [emeralds]", "Trade the amount specified to another player", false);
        return eb.build();
    }

    @Override
    protected ActionRow pageActionRow() {
        return ActionRow.of(WIKI_COMMANDS);
    }

    @Override
    protected String getTitle() {
        return "Commands";
    }

}
