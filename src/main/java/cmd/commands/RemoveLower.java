package cmd.commands;

import collection.CollectionHandler;
import cmd.AbstractCommand;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;

import java.lang.reflect.InvocationTargetException;

public class RemoveLower extends AbstractCommand {
    private CollectionHandler collectionHandler;
    public RemoveLower(CollectionHandler collectionHandler) {
        super("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный", CmdType.COMPLEX_ARG);
        this.collectionHandler=collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        try {
            collectionHandler.removeLower(args.getDeconstructedObject());
            return new ActionResult(true, "Successfully removed all lower objects");
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
            return new ActionResult(false, e.toString());
        }
    }
}
