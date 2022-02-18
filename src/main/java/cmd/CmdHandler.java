package cmd;

import common.CmdRequest;
import common.CmdResponse;

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
    public void addCmds(Command ... comms) {
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
    public CmdResponse executeCmd(CmdRequest request) {
        ActionResult result = request.getCmd().action(request.getCmdArgs());
        return new CmdResponse(result);
    }
}
