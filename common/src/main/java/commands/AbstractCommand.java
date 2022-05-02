package commands;

import collection.meta.CollectibleModel;
import collection.meta.CollectibleScheme;
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
        if (!checkArgsType(executionPayload))
            return new ActionResult(false, "Incorrect args");
        return action(executionPayload);
    }

    protected abstract ActionResult action(ExecutionPayload executionPayload);

    protected boolean checkArgsType(ExecutionPayload executionPayload) {
        CommandArgsType dataArgsType = data.getCommandArgsInfo().getType();
        CommandArgsType payloadArgsType = executionPayload.getCommandArgs().getArgsType();
        if (!dataArgsType.equals(payloadArgsType)) return false;
        if (dataArgsType.equals(CommandArgsType.COMPLEX_ARG) || dataArgsType.equals(CommandArgsType.BOTH_ARG)) {
            CollectibleScheme dataScheme = data.getCommandArgsInfo().getTargetScheme();
            CollectibleModel payloadModel = executionPayload.getCommandArgs().getCollectibleModel();
            if (payloadModel == null) return false;
            CollectibleScheme payloadScheme = payloadModel.getCollectibleScheme();
            return dataScheme.equals(payloadScheme);
        }
        return true;
    }

}
