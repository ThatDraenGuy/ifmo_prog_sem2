package common;

import cmd.CmdArgs;
import cmd.Command;

/**
 * A request for command's execution. Consists of a Command and arguments for it. Handled by a {@link cmd.CmdHandler}
 */
public interface Request {
    Command getCmd();

    CmdArgs getCmdArgs();

    void setCmdArgs(CmdArgs args);
}
