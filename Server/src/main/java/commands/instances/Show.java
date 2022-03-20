package commands.instances;

import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import collection.DragonCollectionHandler;

/**
 * A command for showing collection's elements. Invokes {@link DragonCollectionHandler#toString()}
 */
public class Show extends AbstractCommand {
    private DragonCollectionHandler collectionHandler;

    public Show(DragonCollectionHandler collectionHandler) {
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
