package commands.instances;

import collection.CollectionHandler;
import collection.ServerCollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandArgsType;
import exceptions.StorageException;

import java.util.NoSuchElementException;

/**
 * A command for removing first element from collection. Invokes {@link ServerCollectionHandler#removeFirst()}
 */
public class RemoveFirst extends AbstractCommand {
    private final ServerCollectionHandler<?> collectionHandler;

    public RemoveFirst(ServerCollectionHandler<?> collectionHandler) {
        super("remove_first", "удалить первый элемент из коллекции", CommandArgsType.NO_ARGS);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            collectionHandler.removeFirst();
            return new ActionResult(true, "Successfully deleted first element");
        } catch (NoSuchElementException e) {
            return new ActionResult(false, "There are no elements in the collection!");
        } catch (StorageException e) {
            return new ActionResult(false, "A storage exception has occured: " + e.getMessage());
        }
    }
}
