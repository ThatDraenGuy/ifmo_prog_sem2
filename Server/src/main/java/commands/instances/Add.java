package commands.instances;

import collection.ServerCollectionHandler;
import commands.*;
import exceptions.IncorrectCollectibleTypeException;
import exceptions.StorageException;

/**
 * A command for adding new elements to collection. Receives a deconstructed object on the form of a complex arg and calls
 */
public class Add extends AbstractCommand {
    private final ServerCollectionHandler<?> serverCollectionHandler;

    public Add(ServerCollectionHandler<?> serverCollectionHandler) {
        super("add", "добавить новый элемент в коллекцию", new CommandArgsInfo(CommandArgsType.COMPLEX_ARG, serverCollectionHandler.getCollectibleScheme()));
        this.serverCollectionHandler = serverCollectionHandler;
    }

    @Override
    public ActionResult action(ExecutionPayload executionPayload) {
        try {
            serverCollectionHandler.add(executionPayload.getCommandArgs().getCollectibleModel());
            return new ActionResult(true, "addSuccess");
        } catch (StorageException e) {
            return new ActionResult(false, "storageException");
        } catch (IncorrectCollectibleTypeException e) {
            return new ActionResult(false, "incorrectCollectibleException");
        }
    }
}
