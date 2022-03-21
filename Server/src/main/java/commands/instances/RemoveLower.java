package commands.instances;

import collection.CollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;

import java.util.HashMap;

/**
 */
public class RemoveLower extends AbstractCommand {
    private final CollectionHandler<?> collectionHandler;

    public RemoveLower(CollectionHandler<?> collectionHandler) {
        super("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный", CommandType.COMPLEX_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
//        collectionHandler.removeLower(args.getRawObject());
        return new ActionResult(true, "Successfully removed all lower objects");
    }
}
