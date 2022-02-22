package cmd.commands;

import Collection.CollectionHandler;
import cmd.AbstractCommand;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;

import java.lang.reflect.InvocationTargetException;

public class Add extends AbstractCommand {
    CollectionHandler collectionHandler;
    public Add(CollectionHandler collectionHandler) {
        super("add", "добавить новый элемент в коллекцию", CmdType.COMPLEX_ARG);
        this.collectionHandler=collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        try {
            collectionHandler.addDragon(args.getDeconstructedObject());
            return new ActionResult(true, "Successfully added new dragon to collection");

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return new ActionResult(false, "A critical exception occurred while trying to add a dragon!");
        }
    }
}
