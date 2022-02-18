package cmd.commands;

import cmd.*;

import java.util.HashMap;

public class Help extends Command {
    private CmdHandler cmdHandler;
    public Help(CmdHandler cmdHandler) {
        super("Help", "This is a help command");
        this.cmdType= CmdType.NO_ARGS;
        this.cmdHandler = cmdHandler;
    }
    @Override
    public ActionResult action(CmdArgs args) {
        HashMap<String, Command> cmds = cmdHandler.getCmds();
        cmds.forEach((s, cmd) -> System.out.println(cmd.getName()+" "+cmd.getDescription()));
        return new ActionResult(true, "Displayed cmd list");
    }
}
