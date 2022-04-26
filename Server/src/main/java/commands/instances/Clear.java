package commands.instances;

import collection.CollectionHandler;
import collection.ServerCollectionHandler;
import commands.*;
import exceptions.StorageException;

/**
 * A command for clearing the collection. Invokes {@link ServerCollectionHandler#clear(String)}
 */
public class Clear extends AbstractComplicatedCommand {
    private final ServerCollectionHandler<?> collectionHandler;

    public Clear(ServerCollectionHandler<?> collectionHandler) {
        super("clear", "очистить коллекцию", CommandArgsType.NO_ARGS);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult complicatedAction(ExecutionPayload executionPayload) {
        try {
            collectionHandler.clear(executionPayload.getAccount().getUsername());
            return new ActionResult(true, "Successfully cleared your collectibles");
        } catch (StorageException e) {
            return new ActionResult(false, "A storage exception has occurred: " + e.getMessage());
        }
    }
}
