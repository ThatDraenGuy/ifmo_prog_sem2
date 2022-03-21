package commands.instances;

import collection.CollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import exceptions.ElementIdException;

/**
 * A command for updating element in collection. Invokes
 */
public class Update extends AbstractCommand {
    private CollectionHandler<?> collectionHandler;

    public Update(CollectionHandler<?> collectionHandler) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному", CommandType.BOTH_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
//        try {
//            collectionHandler.update(args.getArgs(), args.getRawObject());
//            return new ActionResult(true, "Successfully updated element with id " + args.getArgs());
//        } catch (ElementIdException e) {
//            return new ActionResult(false, e.getMessage());
//        }
        return null;
    }
}
