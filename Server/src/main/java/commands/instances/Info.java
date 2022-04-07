package commands.instances;

import collection.CollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandArgsType;

/**
 * A command for displaying information about collection. Invokes {@link CollectionHandler#info()}
 */
public class Info extends AbstractCommand {
    private CollectionHandler<?> collectionHandler;

    public Info(CollectionHandler<?> collectionHandler) {
        super("info", "вывести информацию о коллекции", CommandArgsType.NO_ARGS);

        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        return new ActionResult(true, collectionHandler.info());
    }
}
