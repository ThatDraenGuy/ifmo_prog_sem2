package commands;

import java.util.HashMap;

public interface CommandsHandler {
    public void addCommand(Command c);

    public void addCommands(Command... cmds);

    public boolean isInCommands(String name);

    public HashMap<String, Command> getCommands();
}
