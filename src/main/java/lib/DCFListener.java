package lib;

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
        DCFSlashCommand command = dcf.commands().getCommand(event.getCommandIdLong());
        if (command == null) return;
        command.onCommand(event);
    }
}
