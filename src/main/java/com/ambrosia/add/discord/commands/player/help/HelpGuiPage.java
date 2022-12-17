package com.ambrosia.add.discord.commands.player.help;

import com.ambrosia.add.discord.DiscordBot;
import com.ambrosia.add.discord.util.AmbrosiaColor;
import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public abstract class HelpGuiPage extends DCFGuiPage<DCFGui> {

    public static final Button GITHUB_README = Button.link("https://github.com/amp7368/AmbrosiaADD/blob/master/Readme.md", "Readme");
    public static final Button GITHUB = Button.link("https://github.com/amp7368/AmbrosiaADD", "Github");
    public static final Button WIKI = Button.link("https://github.com/amp7368/AmbrosiaADD/wiki", "Wiki");
    public static final Button BLACKJACK_WIKI = Button.link("https://github.com/amp7368/AmbrosiaADD/wiki/Blackjack", "Blackjack");

    public static final Button WIKI_COMMANDS = Button.link("https://github.com/amp7368/AmbrosiaADD/wiki/Commands", "Commands");

    public HelpGuiPage(DCFGui dcfGui) {
        super(dcfGui);
        registerButton("home", (e) -> this.parent.page(0));
        registerButton("commands", (e) -> this.parent.page(1));
        registerButton("games", (e) -> this.parent.page(2));
    }

    private LayoutComponent quickLinks() {
        Button home = Button.primary("home", "Home");
        Button commands = Button.primary("commands", "Commands");
        Button games = Button.primary("games", "Games");
        return ActionRow.of(home, commands, games);
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setThumbnail(DiscordBot.SELF_USER_AVATAR);
        embedBuilder.setColor(this.color());
        embedBuilder.setAuthor(this.getTitle());
        embedBuilder.setTitle(String.format("Help (%d)", getPageNum() + 1));

        MessageEmbed embed = this.makeEmbed(embedBuilder);
        MessageCreateBuilder message = new MessageCreateBuilder().setEmbeds(embed);
        message.setComponents(pageActionRow(), navigationRow());
        return message.build();
    }

    protected int color() {
        return AmbrosiaColor.CASINO_COLOR;
    }

    protected abstract MessageEmbed makeEmbed(EmbedBuilder embedBuilder);

    protected abstract ActionRow pageActionRow();

    protected abstract String getTitle();

    private ActionRow navigationRow() {
        return ActionRow.of(btnFirst(), btnPrev(), btnNext(), btnLast());
    }
}
