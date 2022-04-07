package commands.instances;

import collection.CollectionClassesHandler;
import commands.*;

public class ApplyFullCollection extends AbstractCommand {
    private final CollectionClassesHandler targetClassHandler;

    public ApplyFullCollection(CollectionClassesHandler targetClassHandler) {
        super("apply_full_collection", "internal command for applying full collection", CommandArgsType.NO_ARGS, CommandAccessLevel.INTERNAL);
        this.targetClassHandler = targetClassHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        targetClassHandler.applyFullCollection(args.getFullCollection());
        System.out.println("Collection was changed (new collection)" + args.getFullCollection());
        System.out.println(targetClassHandler.getCurrentCollectionHandler());
        return new ActionResult(true, "successfully applied changes");
    }
}
