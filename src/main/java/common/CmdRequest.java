package common;

import cmd.CmdArgs;
import cmd.Command;

public class CmdRequest implements Request {
    private Command cmd;
    private CmdArgs cmdArgs;
    public CmdRequest(Command cmd, CmdArgs cmdArgs) {
        this.cmd=cmd;
        this.cmdArgs=cmdArgs;
    }

    @Override
    public Command getCmd() {
        return cmd;
    }

    @Override
    public CmdArgs getCmdArgs() {
        return cmdArgs;
    }
}
