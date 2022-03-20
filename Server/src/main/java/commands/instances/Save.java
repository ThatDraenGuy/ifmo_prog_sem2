package commands.instances;

import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import collection.DragonCollectionHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * A command for saving collection. Invokes {@link DragonCollectionHandler#save()}
 */
public class Save extends AbstractCommand {
    private DragonCollectionHandler collectionHandler;

    public Save(DragonCollectionHandler collectionHandler) {
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
