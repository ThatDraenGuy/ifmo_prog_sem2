package commands.instances;

import collection.CollectionBridge;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandArgsType;
import exceptions.ElementIdException;
import exceptions.IncorrectCollectibleTypeException;

/**
 * A command for updating element in collection. Invokes
 */
public class Update extends AbstractCommand {
    private CollectionBridge<?> collectionBridge;

    public Update(CollectionBridge<?> collectionBridge) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному", CommandArgsType.BOTH_ARG);
        this.collectionBridge = collectionBridge;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            collectionBridge.update(args.getArgs(), args.getRawObject());
            return new ActionResult(true, "Successfully updated element with id " + args.getArgs());
        } catch (ElementIdException | IncorrectCollectibleTypeException e) {
            return new ActionResult(false, e.getMessage());
        }
    }
}
