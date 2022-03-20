package commands.instances;

import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import collection.DragonCollectionHandler;

import java.util.HashMap;

/**
 * A command for removing elements lower than specified from collection. Invokes {@link DragonCollectionHandler#removeLower(HashMap)}
 */
public class RemoveLower extends AbstractCommand {
    private final DragonCollectionHandler collectionHandler;

    public RemoveLower(DragonCollectionHandler collectionHandler) {
        super("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный", CommandType.COMPLEX_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        collectionHandler.removeLower(args.getRawObject());
        return new ActionResult(true, "Successfully removed all lower objects");
    }
}
