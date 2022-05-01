package commands;

import lombok.Getter;

public abstract class AbstractCommand implements Command {
    @Getter
    private final CommandData data;

    public AbstractCommand(String name, String desc, CommandArgsInfo info) {
        this.data = new CommandData(name, desc, info);
    }

    public AbstractCommand(String name, String desc, CommandArgsInfo info, CommandAccessLevel accessLevel) {
        this.data = new CommandData(name, desc, info, accessLevel);
    }

    public ActionResult execute(ExecutionPayload executionPayload) {
        if (checkArgsType(executionPayload))
            return new ActionResult(false, "Incorrect argsType");
        return action(executionPayload);
    }

    protected abstract ActionResult action(ExecutionPayload executionPayload);

    protected boolean checkArgsType(ExecutionPayload executionPayload) {
        return !executionPayload.getCommandArgs().getArgsType().equals(data.getCommandArgsInfo().getType());
    }

}
