package commands.instances;

import collection.CollectionBridge;
import collection.CollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import exceptions.IncorrectCollectibleTypeException;

/**
 */
public class RemoveLower extends AbstractCommand {
    private final CollectionBridge<?> collectionBridge;

    public RemoveLower(CollectionBridge<?> collectionBridge) {
        super("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный", CommandType.COMPLEX_ARG);
        this.collectionBridge = collectionBridge;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            collectionBridge.removeLower(args.getRawObject());
            return new ActionResult(true, "Successfully removed all lower objects");
        } catch (IncorrectCollectibleTypeException e) {
            return new ActionResult(false, e.getMessage());
        }
    }
}
