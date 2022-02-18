package common;

import cmd.CmdArgs;
import cmd.Command;

public interface Request {
    Command getCmd();
    CmdArgs getCmdArgs();
}
