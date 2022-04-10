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
//        System.out.println("Collection was changed");
//        System.out.println(targetClassHandler.getCurrentCollectionHandler());
        //TODO remove
        return new ActionResult(true, "successfully applied changes");
    }
}
