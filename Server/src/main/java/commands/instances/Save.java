package commands.instances;

import collection.CollectionHandler;
import commands.*;

import java.io.IOException;

/**
 * A command for saving collection.
 */
public class Save extends AbstractCommand {
    private final CollectionHandler<?> collectionHandler;

    public Save(CollectionHandler<?> collectionHandler) {
        super("save", "сохранить коллекцию в файл", CommandArgsType.NO_ARGS, CommandAccessLevel.DEV);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            collectionHandler.save();
            return new ActionResult(true, "Successfully saved collection");
        } catch (IOException e) {
            return new ActionResult(false, "An exception occurred: " + e.getMessage());
        }
    }
}
