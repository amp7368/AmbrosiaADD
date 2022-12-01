package lib;

import lib.slash.DCFAbstractCommand;
import lib.slash.DCFSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DCFListener extends ListenerAdapter {

    private final DCF dcf;

    public DCFListener(DCF dcf) {
        this.dcf = dcf;
        dcf.jda().addEventListener(this);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        DCFAbstractCommand command = dcf.commands().getCommand(event.getFullCommandName());
        if (command == null) return;
        command.onCommand(event);
    }
}
