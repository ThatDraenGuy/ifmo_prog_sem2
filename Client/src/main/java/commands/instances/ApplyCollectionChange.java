package commands.instances;

import collection.CollectionClassesHandler;
import commands.*;

public class ApplyCollectionChange extends AbstractComplicatedCommand {
    private final CollectionClassesHandler targetClassHandler;

    public ApplyCollectionChange(CollectionClassesHandler collectionClassesHandler) {
        super("apply_collection_change", "internal command for applying collectionChange", CommandArgsType.NO_ARGS, CommandAccessLevel.INTERNAL);
        this.targetClassHandler = collectionClassesHandler;
    }

    @Override
    public ActionResult complicatedAction(ExecutionPayload executionPayload) {
        targetClassHandler.applyCollectionChanges(executionPayload.getCollectionChanges());
        return new ActionResult(true, "successfully applied changes");
    }
}
