package commands.instances;

import commands.AbstractCommand;
import collection.CollectionHandler;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * A command for adding new elements to collection. Receives a deconstructed object on the form of a complex arg and calls {@link collection.CollectionHandler#add(HashMap)}
 */
public class Add extends AbstractCommand {
    CollectionHandler collectionHandler;

    public Add(CollectionHandler collectionHandler) {
        super("add", "добавить новый элемент в коллекцию", CommandType.COMPLEX_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            collectionHandler.add(args.getDeconstructedObject());
            return new ActionResult(true, "Successfully added new dragon to collection");

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
            return new ActionResult(false, e.toString());
        }
    }
}
