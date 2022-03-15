package cmd.commands;

import cmd.AbstractCommand;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;
import collection.CollectionHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * A command for saving collection. Invokes {@link CollectionHandler#save()}
 */
public class Save extends AbstractCommand {
    private CollectionHandler collectionHandler;

    public Save(CollectionHandler collectionHandler) {
        super("save", "сохранить коллекцию в файл", CmdType.NO_ARGS);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        try {
            collectionHandler.save();
            return new ActionResult(true, "Successfully saved collection");
        } catch (IOException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            return new ActionResult(false, "An exception occurred: " + e.getMessage());
        }
    }
}
