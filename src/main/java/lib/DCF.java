package lib;

import net.dv8tion.jda.api.JDA;

public class DCF {

    private final JDA jda;
    private DCFCommandManager commands;
    private DCFListener listener;

    public DCF(JDA jda) {
        this.jda = jda;
        this.commands = new DCFCommandManager(this);
        this.listener = new DCFListener(this);
    }

    public JDA jda() {
        return this.jda;
    }

    public DCFCommandManager commands() {
        return commands;
    }

}
