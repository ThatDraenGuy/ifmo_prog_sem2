package commands.instances;

import collection.CollectionClassesHandler;
import collection.CollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandArgsType;

/**
 * A command for displaying information about collection. Invokes {@link CollectionHandler#info()}
 */
public class Info extends AbstractCommand {
    private final CollectionClassesHandler collectionClassesHandler;

    public Info(CollectionClassesHandler collectionClassesHandler) {
        super("info", "вывести информацию о коллекции", CommandArgsType.NO_ARGS);

        this.collectionClassesHandler = collectionClassesHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        return new ActionResult(true, collectionClassesHandler.getCurrentCollectionHandler().info());
    }
}
