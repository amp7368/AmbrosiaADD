package com.ambrosia.add.discord.commands.casino;

import static com.ambrosia.add.discord.util.Emeralds.longMessage;

import com.ambrosia.add.database.casino.CasinoQuery;
import com.ambrosia.add.database.casino.profits.CasinoTotalProfits;
import com.ambrosia.add.database.operation.TransactionType;
import com.ambrosia.add.discord.util.BaseSubCommand;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

public class CommandCasinoProfits extends BaseSubCommand {

    @Override
    protected void onCheckedCommand(SlashCommandInteractionEvent event) {
        CasinoQuery query = new CasinoQuery();
        CasinoTotalProfits totalProfits = query.totalProfits();
        EmbedBuilder embed = this.embedCasino();
        embed.addField("Profits", longMessage(totalProfits.profits), true);
        embed.addField("Circulated", longMessage(totalProfits.circulated), true);
        embed.addBlankField(true);
        embed.addField("Player holdings", longMessage(totalProfits.playerHoldings), false);
        embed.addBlankField(false);
        for (TransactionType type : List.of(TransactionType.WIN, TransactionType.LOSS))
            embed.addField(getTotalByType(totalProfits, type));
        embed.addBlankField(false);
        for (TransactionType type : List.of(TransactionType.DEPOSIT, TransactionType.WITHDRAW))
            embed.addField(getTotalByType(totalProfits, type));
        event.replyEmbeds(embed.build()).queue();
    }

    @NotNull
    private Field getTotalByType(CasinoTotalProfits totalProfits, TransactionType type) {
        return new Field(type.displayName(), longMessage(totalProfits.totalByType.get(type).sumAmount()), true);
    }

    @Override
    public SubcommandData getData() {
        return new SubcommandData("profits", "Display total profits of the casino");
    }

    @Override
    public boolean isOnlyManager() {
        return true;
    }
}
