package commands.instances;

import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import collection.CollectionHandler;

/**
 * A command for clearing the collection. Invokes {@link CollectionHandler#clear()}
 */
public class Clear extends AbstractCommand {
    private CollectionHandler collectionHandler;

    public Clear(CollectionHandler collectionHandler) {
        super("clear", "очистить коллекцию", CommandType.NO_ARGS);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        collectionHandler.clear();
        return new ActionResult(true, "Successfully cleared collection");
    }
}
