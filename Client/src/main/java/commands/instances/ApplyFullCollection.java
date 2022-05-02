package commands.instances;

import collection.CollectionClassesHandler;
import commands.*;
import exceptions.IncorrectCollectibleTypeException;

public class ApplyFullCollection extends AbstractCommand {
    private final CollectionClassesHandler targetClassHandler;

    public ApplyFullCollection(CollectionClassesHandler targetClassHandler) {
        super("apply_full_collection", "internal command for applying full collection", new CommandArgsInfo(CommandArgsType.NO_ARGS), CommandAccessLevel.INTERNAL);
        this.targetClassHandler = targetClassHandler;
    }

    @Override
    public ActionResult action(ExecutionPayload executionPayload) {
        try {
            targetClassHandler.applyFullCollection(executionPayload.getCollection());
            return new ActionResult(true, "successfully applied changes");
        } catch (IncorrectCollectibleTypeException e) {
            return new ActionResult(false, e.getMessage());
        }
    }
}
