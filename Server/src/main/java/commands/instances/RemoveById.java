package commands.instances;

import collection.CollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandArgsType;
import exceptions.ElementIdException;

/**
 * A command for removing element by its id. Invokes {@link DragonCollectionHandler#removeById(String)}
 */
public class RemoveById extends AbstractCommand {
    CollectionHandler<?> collectionHandler;

    public RemoveById(CollectionHandler<?> collectionHandler) {
        super("remove_by_id", "удалить элемент из коллекции по его id", CommandArgsType.SIMPLE_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            collectionHandler.removeById(args.getArgs());
            return new ActionResult(true, "Successfully deleted element with id " + args.getArgs());
        } catch (ElementIdException e) {
            return new ActionResult(false, e.getMessage());
        }
    }
}
