package lib;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class DCFSlashCommand {


    public abstract CommandData getData();

    public abstract void onCommand(SlashCommandInteractionEvent event);

}
