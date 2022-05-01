package commands.instances;

import collection.ServerCollectionHandler;
import commands.*;
import exceptions.StorageException;

import java.util.NoSuchElementException;

/**
 * A command for removing first element from collection. Invokes {@link ServerCollectionHandler#removeFirst(String)}
 */
public class RemoveFirst extends AbstractCommand {
    private final ServerCollectionHandler<?> collectionHandler;

    public RemoveFirst(ServerCollectionHandler<?> collectionHandler) {
        super("remove_first", "удалить первый элемент из коллекции", new CommandArgsInfo(CommandArgsType.NO_ARGS));
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(ExecutionPayload executionPayload) {
        try {
            collectionHandler.removeFirst(executionPayload.getAccount().getName());
            return new ActionResult(true, "Successfully deleted first element");
        } catch (NoSuchElementException e) {
            return new ActionResult(false, "There are no elements in the collection!");
        } catch (StorageException e) {
            return new ActionResult(false, "A storage exception has occured: " + e.getMessage());
        }
    }
}
