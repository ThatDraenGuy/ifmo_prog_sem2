package commands.instances;

import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import collection.CollectionHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * A command for removing elements lower than specified from collection. Invokes {@link CollectionHandler#removeLower(HashMap)}
 */
public class RemoveLower extends AbstractCommand {
    private CollectionHandler collectionHandler;

    public RemoveLower(CollectionHandler collectionHandler) {
        super("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный", CommandType.COMPLEX_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            collectionHandler.removeLower(args.getDeconstructedObject());
            return new ActionResult(true, "Successfully removed all lower objects");
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
            return new ActionResult(false, e.toString());
        }
    }
}
