package commands.instances;

import collection.CollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandArgsType;

/**
 * A command for clearing the collection. Invokes {@link DragonCollectionHandler#clear()}
 */
public class Clear extends AbstractCommand {
    private CollectionHandler<?> collectionHandler;

    public Clear(CollectionHandler<?> collectionHandler) {
        super("clear", "очистить коллекцию", CommandArgsType.NO_ARGS);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        collectionHandler.clear();
        return new ActionResult(true, "Successfully cleared collection");
    }
}
