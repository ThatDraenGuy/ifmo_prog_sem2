package commands.instances;

import collection.CollectionClassesHandler;
import commands.*;
import exceptions.IncorrectCollectibleTypeException;

public class ApplyCollectionChange extends AbstractCommand {
    private final CollectionClassesHandler targetClassHandler;

    public ApplyCollectionChange(CollectionClassesHandler collectionClassesHandler) {
        super("apply_collection_change", "internal command for applying collectionChange", new CommandArgsInfo(CommandArgsType.NO_ARGS), CommandAccessLevel.INTERNAL);
        this.targetClassHandler = collectionClassesHandler;
    }

    @Override
    public ActionResult action(ExecutionPayload executionPayload) {
        try {
            targetClassHandler.applyCollectionChanges(executionPayload.getCollectionChanges());
            return new ActionResult(true, "successfully applied changes");
        } catch (IncorrectCollectibleTypeException e) {
            return new ActionResult(false, e.getMessage());
        }
    }
}
