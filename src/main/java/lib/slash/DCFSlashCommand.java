package lib.slash;

import java.util.Collections;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class DCFSlashCommand extends DCFAbstractCommand{

    public abstract SlashCommandData getData();

    public final SlashCommandData getFullData() {
        return getData().addSubcommands(getSubCommands().stream().map(DCFSlashSubCommand::getData).toList());
    }

    public List<DCFSlashSubCommand> getSubCommands() {
        return Collections.emptyList();
    }

}
