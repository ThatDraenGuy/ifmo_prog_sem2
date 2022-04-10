package commands.instances;

import collection.CollectionClassesHandler;
import commands.*;

public class ApplyCollectionChange extends AbstractCommand {
    private final CollectionClassesHandler targetClassHandler;

    public ApplyCollectionChange(CollectionClassesHandler collectionClassesHandler) {
        super("apply_collection_change", "internal command for applying collectionChange", CommandArgsType.NO_ARGS, CommandAccessLevel.INTERNAL);
        this.targetClassHandler = collectionClassesHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        targetClassHandler.applyCollectionChanges(args.getCollectionChanges());
        return new ActionResult(true, "successfully applied changes");
    }
}
