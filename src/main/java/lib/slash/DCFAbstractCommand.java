package lib.slash;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class DCFAbstractCommand {

    public abstract void onCommand(SlashCommandInteractionEvent event);

}
