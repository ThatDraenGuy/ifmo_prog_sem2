package commands.instances;

import commands.*;

import java.util.Collection;

/**
 * A History command. Displays 5 last used commands.
 */
public class History extends AbstractCommand {
    private final ExecutionController executionController;

    public History(ExecutionController executionController) {
        super("history", "вывести последние 5 команд", new CommandArgsInfo(CommandArgsType.NO_ARGS));
        this.executionController = executionController;
    }

    @Override
    public ActionResult action(ExecutionPayload executionPayload) {
        Collection<CommandData> history = executionController.getHistory();
        StringBuilder str = new StringBuilder();
        for (CommandData data : history) {
            if (data != null) {
                str.append(data.getName()).append("\n");
            }
        }
        return new ActionResult(true, str.toString());
    }
}
