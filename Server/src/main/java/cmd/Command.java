package cmd;

public interface Command {
    /**
     * Performs command's action
     *
     * @param args command's arguments/parameters needed to perform its action
     * @return command's action result
     */
    ActionResult action(CmdArgs args);

    String getName();

    String getDescription();

    CmdType getCmdType();
}
