package commands;

public interface Command {
    /**
     * Performs command's action
     *
     * @param args command's arguments/parameters needed to perform its action
     * @return command's action result
     */
    ActionResult action(CommandArgs args);

    String getName();

    String getDescription();

    CommandType getCommandType();
}
