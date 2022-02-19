package cmd;

public interface Command {
    ActionResult action(CmdArgs args);
    String getName();
    String getDescription();
    CmdType getCmdType();
}
