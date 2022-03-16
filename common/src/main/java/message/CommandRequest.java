package message;

import commands.CommandArgs;
import commands.Command;

public class CommandRequest implements Request {
    private Command cmd;
    private CommandArgs cmdArgs;

    public CommandRequest(Command cmd, CommandArgs cmdArgs) {
        this.cmd = cmd;
        this.cmdArgs = cmdArgs;
    }

    @Override
    public void setCommandArgs(CommandArgs cmdArgs) {
        this.cmdArgs = cmdArgs;
    }

    @Override
    public Command getCommand() {
        return cmd;
    }

    @Override
    public CommandArgs getCommandArgs() {
        return cmdArgs;
    }
}
