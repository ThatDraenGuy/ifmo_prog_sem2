package cmd;

import Exceptions.CmdArgsAmountException;
import common.CmdResponse;
import common.Request;

import java.util.HashMap;

public class CmdHandler {
    protected HashMap<String, Command> cmds;
    public CmdHandler() {
        this.cmds=new HashMap<>();
    }
    public void addComm(Command c) {
        final String name = c.getName();
        if (!isInCmds(name)) {
            this.cmds.put(name,c);
        }
    }
    public void addCmds(Command... comms) {
        for (Command c : comms) {
            addComm(c);
        }
    }
    public boolean isInCmds(String name) {
        return this.cmds.containsKey(name);
    }

    public HashMap<String, Command> getCmds() {
        return cmds;
    }
    public CmdResponse executeCmd(Request request) throws CmdArgsAmountException {
        Command cmd = request.getCmd();
        CmdArgs cmdArgs = request.getCmdArgs();
        if (cmd.getCmdType()==CmdType.NO_ARGS && cmdArgs.getArgs()!="") {
            throw new CmdArgsAmountException("Command \""+cmd.getName()+"\" does not need arguments!");
        } else if (cmd.getCmdType()==CmdType.SIMPLE_ARG && cmdArgs.getArgs()=="") {
            throw new CmdArgsAmountException(cmd.getName()+" needs an argument!");
        }
        ActionResult result = cmd.action(cmdArgs);
        return new CmdResponse(result);
    }
}
