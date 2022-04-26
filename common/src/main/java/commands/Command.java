package commands;


public interface Command {
    /**
     * Performs command's action
     *
     * @param executionPayload command's arguments/parameters needed to perform its action
     * @return command's action result
     */
    ActionResult execute(ExecutionPayload executionPayload);

    CommandData getData();
}
