package commands.instances;

import commands.AbstractCommand;
import collection.DragonCollectionHandler;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;

import java.util.NoSuchElementException;

/**
 * A command for removing first element from collection. Invokes {@link DragonCollectionHandler#removeFirst()}
 */
public class RemoveFirst extends AbstractCommand {
    DragonCollectionHandler collectionHandler;

    public RemoveFirst(DragonCollectionHandler collectionHandler) {
        super("remove_first", "удалить первый элемент из коллекции", CommandType.NO_ARGS);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            collectionHandler.removeFirst();
            return new ActionResult(true, "Successfully deleted first element");
        } catch (NoSuchElementException e) {
            return new ActionResult(false, "There are no elements in the collection!");
        }
    }
}
