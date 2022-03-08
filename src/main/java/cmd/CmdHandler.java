package cmd;

import collection.CollectionHandler;
import exceptions.CmdArgsAmountException;
import common.CmdResponse;
import common.Request;
import common.Response;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * A class needed to handle all the commands. Stores all commands and provides access to them. Stores command history.
 * Accepts {@link common.Request} from {@link console.ConsoleHandler} and returns a {@link common.Response} after performing command's action.
 * Also works as a link between commands and {@link collection.CollectionHandler};
 */
public class CmdHandler {
    private final HashMap<String, Command> cmds;
    private final LinkedList<Command> cmdHistory;
    private final CollectionHandler collectionHandler;
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

    /**
     * @param request Accepts a request from {@link console.ConsoleHandler}
     * @return a response containing result of command's action.
     * @throws CmdArgsAmountException if arguments form a request don't match {@link cmd.CmdType} of the command
     */
    public Response executeCmd(Request request) throws CmdArgsAmountException {
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
