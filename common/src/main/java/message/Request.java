package message;

import commands.CommandArgs;
import commands.Command;
import commands.CommandData;

import java.io.Serializable;

/**
 * A request for command's execution. Consists of a Command and arguments for it. Handled by a ...
 */
public interface Request extends Serializable {
    CommandData getCommandData();

    CommandArgs getCommandArgs();

}
