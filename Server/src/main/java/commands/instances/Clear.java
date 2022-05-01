package commands.instances;

import collection.ServerCollectionHandler;
import commands.*;
import exceptions.StorageException;

/**
 * A command for clearing the collection. Invokes {@link ServerCollectionHandler#clear(String)}
 */
public class Clear extends AbstractCommand {
    private final ServerCollectionHandler<?> collectionHandler;

    public Clear(ServerCollectionHandler<?> collectionHandler) {
        super("clear", "очистить коллекцию", new CommandArgsInfo(CommandArgsType.NO_ARGS));
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(ExecutionPayload executionPayload) {
        try {
            collectionHandler.clear(executionPayload.getAccount().getName());
            return new ActionResult(true, "Successfully cleared your collectibles");
        } catch (StorageException e) {
            return new ActionResult(false, "A storage exception has occurred: " + e.getMessage());
        }
    }
}
