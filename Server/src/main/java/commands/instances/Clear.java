package commands.instances;

import collection.CollectionHandler;
import collection.ServerCollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandArgsType;

/**
 * A command for clearing the collection. Invokes {@link CollectionHandler#clear()}
 */
public class Clear extends AbstractCommand {
    private final ServerCollectionHandler<?> collectionHandler;

    public Clear(ServerCollectionHandler<?> collectionHandler) {
        super("clear", "очистить коллекцию", CommandArgsType.NO_ARGS);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        collectionHandler.clear();
        return new ActionResult(true, "Successfully cleared collection");
    }
}
