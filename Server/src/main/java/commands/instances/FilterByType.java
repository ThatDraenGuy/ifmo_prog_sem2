package commands.instances;

import collection.CollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;

/**
 * A command to filter elements by type. Invokes {@link DragonCollectionHandler#filterByType(String)}
 */
public class FilterByType extends AbstractCommand {
    private CollectionHandler<?> collectionHandler;

    public FilterByType(CollectionHandler<?> collectionHandler) {
        super("filter_by_type", "вывести элементы, значение поля type которых равно заданному", CommandType.SIMPLE_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            String res = collectionHandler.filterByType(args.getArgs());
            return new ActionResult(true, res);
        } catch (IllegalArgumentException e) {
            return new ActionResult(false, "No such DragonType value: \'" + args.getArgs() + "\"");
        }
    }
}
