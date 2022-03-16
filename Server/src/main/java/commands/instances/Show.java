package commands.instances;

import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import collection.CollectionHandler;

/**
 * A command for showing collection's elements. Invokes {@link CollectionHandler#toString()}
 */
public class Show extends AbstractCommand {
    private CollectionHandler collectionHandler;

    public Show(CollectionHandler collectionHandler) {
        super("show", "вывести все элементы коллекции в строковом представлении", CommandType.NO_ARGS);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        String str = collectionHandler.toString();
        if (str != "") {
            return new ActionResult(true, collectionHandler.toString());
        } else {
            return new ActionResult(true, "There are no elements in the collection");
        }
    }
}
