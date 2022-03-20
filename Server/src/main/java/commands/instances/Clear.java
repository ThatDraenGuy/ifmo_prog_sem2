package commands.instances;

import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import collection.DragonCollectionHandler;

/**
 * A command for clearing the collection. Invokes {@link DragonCollectionHandler#clear()}
 */
public class Clear extends AbstractCommand {
    private DragonCollectionHandler collectionHandler;

    public Clear(DragonCollectionHandler collectionHandler) {
        super("clear", "очистить коллекцию", CommandType.NO_ARGS);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        collectionHandler.clear();
        return new ActionResult(true, "Successfully cleared collection");
    }
}
