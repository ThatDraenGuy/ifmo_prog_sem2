package commands.instances;

import collection.CollectionHandler;
import collection.ServerCollectionHandler;
import commands.*;
import exceptions.ElementIdException;
import exceptions.StorageException;

/**
 * A command for removing element by its id. Invokes {@link ServerCollectionHandler#removeById(String, String)}
 */
public class RemoveById extends AbstractComplicatedCommand {
    private final ServerCollectionHandler<?> collectionHandler;

    public RemoveById(ServerCollectionHandler<?> collectionHandler) {
        super("remove_by_id", "удалить элемент из коллекции по его id", CommandArgsType.SIMPLE_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult complicatedAction(ExecutionPayload executionPayload) {
        CommandArgs args = executionPayload.getCommandArgs();
        try {
            collectionHandler.removeById(args.getArgs(), executionPayload.getAccount().getUsername());
            return new ActionResult(true, "Successfully deleted element with id " + args.getArgs());
        } catch (ElementIdException e) {
            return new ActionResult(false, e.getMessage());
        } catch (StorageException e) {
            return new ActionResult(false, "A storage exception has occurred: " + e.getMessage());
        }
    }
}
