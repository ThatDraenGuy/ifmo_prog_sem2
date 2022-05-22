package commands.instances;

import collection.ServerCollectionHandler;
import commands.*;
import exceptions.ElementIdException;
import exceptions.IncorrectCollectibleTypeException;
import exceptions.StorageException;

/**
 * A command for updating element in collection. Invokes
 */
public class Update extends AbstractCommand {
    private final ServerCollectionHandler<?> serverCollectionHandler;

    public Update(ServerCollectionHandler<?> serverCollectionHandler) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному",
                new CommandArgsInfo(CommandArgsType.BOTH_ARG, serverCollectionHandler.getCollectibleScheme()));
        this.serverCollectionHandler = serverCollectionHandler;
    }

    @Override
    public ActionResult action(ExecutionPayload executionPayload) {
        CommandArgs args = executionPayload.getCommandArgs();
        try {
            serverCollectionHandler.update(args.getArgs(), executionPayload.getAccount().getName(), args.getCollectibleModel());
            return new ActionResult(true, "updateSuccess");
        } catch (ElementIdException e) {
            return new ActionResult(false, "elementIdException", e.getMessage());
        } catch (StorageException e) {
            return new ActionResult(false, "storageException");
        } catch (IncorrectCollectibleTypeException e) {
            return new ActionResult(false, "incorrectCollectibleException");
        }
    }
}
