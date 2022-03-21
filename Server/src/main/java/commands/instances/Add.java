package commands.instances;

import collection.CollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;

/**
 * A command for adding new elements to collection. Receives a deconstructed object on the form of a complex arg and calls
 */
public class Add extends AbstractCommand {
    CollectionHandler<?> collectionHandler;

    public Add(CollectionHandler<?> collectionHandler) {
        super("add", "добавить новый элемент в коллекцию", CommandType.COMPLEX_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
//        collectionHandler.add(args.getRawObject());
        return new ActionResult(true, "Successfully added new dragon to collection");
    }
}
