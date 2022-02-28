package cmd.commands;

import Collection.CollectionHandler;
import cmd.AbstractCommand;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * A command for adding new elements to collection. Receives a deconstructed object on the form of a complex arg and calls {@link Collection.CollectionHandler#add(HashMap)}
 */
public class Add extends AbstractCommand {
    CollectionHandler collectionHandler;
    public Add(CollectionHandler collectionHandler) {
        super("add", "добавить новый элемент в коллекцию", CmdType.COMPLEX_ARG);
        this.collectionHandler=collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        try {
            collectionHandler.add(args.getDeconstructedObject());
            return new ActionResult(true, "Successfully added new dragon to collection");

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
            return new ActionResult(false, e.toString());
        }
    }
}
