package commands.instances;

import commands.*;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A History command. Displays 5 last used commands.
 */
public class History extends AbstractCommand {
    private CommandsExecutor commandsExecutor;

    public History(CommandsExecutor commandsExecutor) {
        super("history", "вывести последние 5 команд", CommandArgsType.NO_ARGS);
        this.commandsExecutor = commandsExecutor;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        Collection<CommandData> history = commandsExecutor.getHistory();
        StringBuilder str = new StringBuilder();
        for (CommandData data : history) {
            if (data != null) {
                str.append(data.getName()).append("\n");
            }
        }
        return new ActionResult(true, str.toString());
    }
}
