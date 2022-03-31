package commands.instances;

import commands.*;
import console.ConsoleHandler;

/**
 * A command to exit the program
 */
public class Exit extends AbstractCommand {
    private final ConsoleHandler consoleHandler;
    private final CommandsExecutor commandsExecutor;

    public Exit(ConsoleHandler consoleHandler, CommandsExecutor commandsExecutor) {
        super("exit", "завершить программу (без сохранения в файл)", CommandArgsType.NO_ARGS, true);
        this.consoleHandler = consoleHandler;
        this.commandsExecutor = commandsExecutor;
    }

    public ActionResult action(CommandArgs args) {
        if (consoleHandler.promptAgreement("Are you sure you want to exit?")) {
            commandsExecutor.disconnect();
            return new ActionResult(true, "exiting...", true);
        } else return new ActionResult(false, "Exit cancelled");
    }
}
