package commands;

import java.io.Serializable;

public interface Command extends Serializable {
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
