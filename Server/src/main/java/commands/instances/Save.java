package commands.instances;

import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import collection.CollectionHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * A command for saving collection. Invokes {@link CollectionHandler#save()}
 */
public class Save extends AbstractCommand {
    private CollectionHandler collectionHandler;

    public Save(CollectionHandler collectionHandler) {
        super("save", "сохранить коллекцию в файл", CommandType.NO_ARGS);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            collectionHandler.save();
            return new ActionResult(true, "Successfully saved collection");
        } catch (IOException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            return new ActionResult(false, "An exception occurred: " + e.getMessage());
        }
    }
}
