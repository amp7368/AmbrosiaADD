package lib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class DCFCommandManager {

    private final Map<String, DCFSlashCommand> data = new HashMap<>();
    private final DCF dcf;
    private final Map<Long, DCFSlashCommand> commands = new HashMap<>();

    public DCFCommandManager(DCF dcf) {
        this.dcf = dcf;
    }

    public void addCommand(DCFSlashCommand... commands) {
        synchronized (this.data) {
            for (DCFSlashCommand command : commands) {
                this.data.put(command.getData().getName(), command);
            }
        }
    }

    public void updateCommands() {
        synchronized (this.data) {
            List<CommandData> dataList = data.values().stream().map(DCFSlashCommand::getData).toList();
            dcf.jda().updateCommands().addCommands(dataList).queue(this::setCommands);
        }
    }

    private void setCommands(List<Command> commands) {
        synchronized (this.commands) {
            for (Command command : commands) {
                this.commands.put(command.getIdLong(), this.data.get(command.getFullCommandName()));
            }
        }

    }

    public DCFSlashCommand getCommand(long commandId) {
        synchronized (this.commands) {
            return this.commands.get(commandId);
        }
    }
}
