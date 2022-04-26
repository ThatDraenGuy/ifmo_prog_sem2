package commands;

import lombok.Getter;

public abstract class AbstractCommand implements Command {
    @Getter
    private final CommandData data;

    public AbstractCommand(String name, String desc, CommandArgsType type) {
        this.data = new CommandData(name, desc, type);
    }

    public AbstractCommand(String name, String desc, CommandArgsType type, CommandAccessLevel accessLevel) {
        this.data = new CommandData(name, desc, type, accessLevel);
    }

    public ActionResult execute(ExecutionPayload executionPayload) {
        if (checkArgsType(executionPayload))
            return new ActionResult(false, "Incorrect argsType");
        return action(executionPayload.getCommandArgs());
    }

    protected abstract ActionResult action(CommandArgs commandArgs);

    protected boolean checkArgsType(ExecutionPayload executionPayload) {
        return !executionPayload.getCommandArgs().getArgsType().equals(data.getCommandArgsType());
    }

}
