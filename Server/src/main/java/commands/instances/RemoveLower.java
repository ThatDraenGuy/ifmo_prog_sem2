package commands.instances;

import collection.ServerCollectionHandler;
import commands.*;
import exceptions.StorageException;

/**
 *
 */
public class RemoveLower extends AbstractCommand {
    private final ServerCollectionHandler<?> serverCollectionHandler;

    public RemoveLower(ServerCollectionHandler<?> serverCollectionHandler) {
        super("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный",
                new CommandArgsInfo(CommandArgsType.COMPLEX_ARG, serverCollectionHandler.getCollectibleScheme()));
        this.serverCollectionHandler = serverCollectionHandler;
    }

    @Override
    public ActionResult action(ExecutionPayload executionPayload) {
        CommandArgs args = executionPayload.getCommandArgs();
        try {
            serverCollectionHandler.removeLower(args.getCollectibleModel(), executionPayload.getAccount().getName());
            return new ActionResult(true, "removeSuccess");
        } catch (StorageException e) {
            return new ActionResult(false, "storageException");
        }
    }
}
