package cmd.commands;

import cmd.AbstractCommand;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;
import collection.CollectionHandler;
import exceptions.ElementIdException;

/**
 * A command for removing element by its id. Invokes {@link CollectionHandler#removeById(String)}
 */
public class RemoveById extends AbstractCommand {
    CollectionHandler collectionHandler;

    public RemoveById(CollectionHandler collectionHandler) {
        super("remove_by_id", "удалить элемент из коллекции по его id", CmdType.SIMPLE_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        try {
            collectionHandler.removeById(args.getArgs());
            return new ActionResult(true, "Successfully deleted element with id " + args.getArgs());
        } catch (ElementIdException e) {
            return new ActionResult(false, e.getMessage());
        }
    }
}
