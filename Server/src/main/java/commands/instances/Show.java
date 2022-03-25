package commands.instances;

import collection.CollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;

/**
 * A command for showing collection's elements. Invokes {@link CollectionHandler#toString()}
 */
public class Show extends AbstractCommand {
    private final CollectionHandler<?> collectionHandler;

    public Show(CollectionHandler<?> collectionHandler) {
        super("show", "вывести все элементы коллекции в строковом представлении", CommandType.NO_ARGS);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        String str = collectionHandler.toString();
        if (!str.equals("")) {
            return new ActionResult(true, collectionHandler.toString());
        } else {
            return new ActionResult(true, "There are no elements in the collection");
        }
    }
}
