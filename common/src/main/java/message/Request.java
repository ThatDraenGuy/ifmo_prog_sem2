package message;

import commands.CommandArgs;
import commands.Command;

/**
 * A request for command's execution. Consists of a Command and arguments for it. Handled by a ...
 */
public interface Request {
    Command getCommand();

    CommandArgs getCommandArgs();

    void setCommandArgs(CommandArgs args);
}
