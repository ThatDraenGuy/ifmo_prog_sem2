package commands.instances;

import commands.*;
import console.ConsoleHandler;
import threads.ThreadHandler;

/**
 * A command to exit the program
 */
public class Exit extends AbstractCommand {
    private final ConsoleHandler consoleHandler;
    private final ThreadHandler threadHandler;

    public Exit(ConsoleHandler consoleHandler, ThreadHandler threadHandler) {
        super("exit", "выйти из приложения", new CommandArgsInfo(CommandArgsType.NO_ARGS), CommandAccessLevel.DISCONNECTED);
        this.consoleHandler = consoleHandler;
        this.threadHandler = threadHandler;
    }

    public ActionResult action(ExecutionPayload executionPayload) {
        threadHandler.stop();
        return new ActionResult(true, "exiting...");
    }
}
