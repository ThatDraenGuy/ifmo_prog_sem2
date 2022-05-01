package commands.instances;

import collection.CollectionClassesHandler;
import collection.CollectionHandler;
import commands.*;

/**
 * A command for showing collection's elements. Invokes {@link CollectionHandler#toString()}
 */
public class Show extends AbstractCommand {
    private final CollectionClassesHandler collectionClassesHandler;

    public Show(CollectionClassesHandler collectionClassesHandler) {
        super("show", "вывести все элементы коллекции в строковом представлении", new CommandArgsInfo(CommandArgsType.NO_ARGS));
        this.collectionClassesHandler = collectionClassesHandler;
    }

    @Override
    public ActionResult action(ExecutionPayload executionPayload) {
        String str = collectionClassesHandler.getCurrentCollectionHandler().toString();
        if (!str.equals("")) {
            return new ActionResult(true, str);
        } else {
            return new ActionResult(true, "There are no elements in the collection");
        }
    }
}
