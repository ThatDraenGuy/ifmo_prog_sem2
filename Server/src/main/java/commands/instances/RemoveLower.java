package commands.instances;

import collection.CollectionBridge;
import commands.*;
import exceptions.IncorrectCollectibleTypeException;
import exceptions.StorageException;

/**
 *
 */
public class RemoveLower extends AbstractComplicatedCommand {
    private final CollectionBridge<?> collectionBridge;

    public RemoveLower(CollectionBridge<?> collectionBridge) {
        super("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный", CommandArgsType.COMPLEX_ARG);
        this.collectionBridge = collectionBridge;
    }

    @Override
    public ActionResult complicatedAction(ExecutionPayload executionPayload) {
        CommandArgs args = executionPayload.getCommandArgs();
        try {
            collectionBridge.removeLower(args.getCollectibleModel(), executionPayload.getAccount().getUsername());
            return new ActionResult(true, "Successfully removed all lower objects");
        } catch (IncorrectCollectibleTypeException e) {
            return new ActionResult(false, e.getMessage());
        } catch (StorageException e) {
            return new ActionResult(false, "A storage exception has occurred: " + e.getMessage());
        }
    }
}
