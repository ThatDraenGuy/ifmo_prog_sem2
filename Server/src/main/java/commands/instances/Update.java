package commands.instances;

import collection.CollectionBridge;
import commands.*;
import exceptions.ElementIdException;
import exceptions.IncorrectCollectibleTypeException;
import exceptions.StorageException;

/**
 * A command for updating element in collection. Invokes
 */
public class Update extends AbstractComplicatedCommand {
    private final CollectionBridge<?> collectionBridge;

    public Update(CollectionBridge<?> collectionBridge) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному", CommandArgsType.BOTH_ARG);
        this.collectionBridge = collectionBridge;
    }

    @Override
    public ActionResult complicatedAction(ExecutionPayload executionPayload) {
        CommandArgs args = executionPayload.getCommandArgs();
        try {
            collectionBridge.update(args.getArgs(), executionPayload.getAccount().getUsername(), args.getCollectibleModel());
            return new ActionResult(true, "Successfully updated element with id " + args.getArgs());
        } catch (ElementIdException | IncorrectCollectibleTypeException e) {
            return new ActionResult(false, e.getMessage());
        } catch (StorageException e) {
            return new ActionResult(false, "A storage exception has occurred: " + e.getMessage());
        }
    }
}
