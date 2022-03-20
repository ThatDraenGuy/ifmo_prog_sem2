package commands.instances;

import commands.AbstractCommand;
import collection.DragonCollectionHandler;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;

/**
 * A command for adding new elements to collection. Receives a deconstructed object on the form of a complex arg and calls
 */
public class Add extends AbstractCommand {
    DragonCollectionHandler collectionHandler;

    public Add(DragonCollectionHandler collectionHandler) {
        super("add", "добавить новый элемент в коллекцию", CommandType.COMPLEX_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        collectionHandler.add(args.getRawObject());
        return new ActionResult(true, "Successfully added new dragon to collection");
    }
}
