package common;

import cmd.CmdArgs;
import cmd.AbstractCommand;
import cmd.Command;

public interface Request {
    Command getCmd();
    CmdArgs getCmdArgs();
}
