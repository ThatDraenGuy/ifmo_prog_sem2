package commands;

public abstract class AbstractComplicatedCommand extends AbstractCommand {


    public AbstractComplicatedCommand(String name, String desc, CommandArgsType type) {
        super(name, desc, type);
    }

    public AbstractComplicatedCommand(String name, String desc, CommandArgsType type, CommandAccessLevel accessLevel) {
        super(name, desc, type, accessLevel);
    }

    @Override
    public ActionResult execute(ExecutionPayload executionPayload) {
        if (checkArgsType(executionPayload))
            return new ActionResult(false, "Incorrect argsType");
        return complicatedAction(executionPayload);
    }


    @Override
    protected final ActionResult action(CommandArgs commandArgs) {
        return null;
    }

    protected abstract ActionResult complicatedAction(ExecutionPayload executionPayload);
}
