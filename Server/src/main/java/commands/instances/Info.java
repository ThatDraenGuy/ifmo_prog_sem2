package commands.instances;

import commands.AbstractCommand;
import collection.DragonCollectionHandler;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;

/**
 * A command for displaying information about collection. Invokes {@link DragonCollectionHandler#info()}
 */
public class Info extends AbstractCommand {
    private DragonCollectionHandler collectionHandler;

    public Info(DragonCollectionHandler collectionHandler) {
        super("info", "вывести информацию о коллекции", CommandType.NO_ARGS);

        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        return new ActionResult(true, collectionHandler.info());
    }
}
