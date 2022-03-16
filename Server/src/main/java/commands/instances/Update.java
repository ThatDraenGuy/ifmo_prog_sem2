package commands.instances;

import commands.AbstractCommand;
import collection.CollectionHandler;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import exceptions.ElementIdException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * A command for updating element in collection. Invokes {@link CollectionHandler#update(String, HashMap)}
 */
public class Update extends AbstractCommand {
    private CollectionHandler collectionHandler;

    public Update(CollectionHandler collectionHandler) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному", CommandType.BOTH_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            collectionHandler.update(args.getArgs(), args.getDeconstructedObject());
            return new ActionResult(true, "Successfully updated element with id " + args.getArgs());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            return new ActionResult(false, e.toString());
        } catch (ElementIdException e) {
            return new ActionResult(false, e.getMessage());
        }

    }
}
