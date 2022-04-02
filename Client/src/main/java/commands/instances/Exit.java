package commands.instances;

import commands.*;
import console.ConsoleHandler;

/**
 * A command to exit the program
 */
public class Exit extends AbstractCommand {
    private final ConsoleHandler consoleHandler;
    private final ExecutionController commandsExecutor;

    public Exit(ConsoleHandler consoleHandler, ExecutionController executionController) {
        super("exit", "выйти из приложения", CommandArgsType.NO_ARGS, CommandAccessLevel.DISCONNECTED, true);
        this.consoleHandler = consoleHandler;
        this.commandsExecutor = executionController;
    }

    public ActionResult action(CommandArgs args) {
        if (consoleHandler.promptAgreement("Are you sure you want to exit?")) {
            commandsExecutor.exit();
            return new ActionResult(true, "exiting...", true);
        } else return new ActionResult(false, "Exit cancelled");
    }
}
