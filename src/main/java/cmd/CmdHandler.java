package cmd;

import Collection.CollectionHandler;
import Exceptions.CmdArgsAmountException;
import common.CmdResponse;
import common.Request;

import java.util.HashMap;
import java.util.LinkedList;

public class CmdHandler {
    private HashMap<String, Command> cmds;
    private LinkedList<Command> cmdHistory;
    private CollectionHandler collectionHandler;
    public CmdHandler(CollectionHandler collectionHandler) {
        this.cmds=new HashMap<>();
        this.collectionHandler=collectionHandler;
        this.cmdHistory=new LinkedList<>();
        for (int i=0; i<5; i++) {
            cmdHistory.add(null);
        }
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

    public LinkedList<Command> getCmdHistory() {
        return cmdHistory;
    }

    public CollectionHandler getCollectionHandler() {
        return collectionHandler;
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
        cmdHistory.addLast(cmd);
        cmdHistory.removeFirst();
        return new CmdResponse(result);
    }
}
