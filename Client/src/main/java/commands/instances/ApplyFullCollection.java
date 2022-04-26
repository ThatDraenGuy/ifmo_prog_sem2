package commands.instances;

import collection.CollectionClassesHandler;
import commands.*;

public class ApplyFullCollection extends AbstractComplicatedCommand {
    private final CollectionClassesHandler targetClassHandler;

    public ApplyFullCollection(CollectionClassesHandler targetClassHandler) {
        super("apply_full_collection", "internal command for applying full collection", CommandArgsType.NO_ARGS, CommandAccessLevel.INTERNAL);
        this.targetClassHandler = targetClassHandler;
    }

    @Override
    public ActionResult complicatedAction(ExecutionPayload executionPayload) {
        targetClassHandler.applyFullCollection(executionPayload.getCollection());
        return new ActionResult(true, "successfully applied changes");
    }
}
