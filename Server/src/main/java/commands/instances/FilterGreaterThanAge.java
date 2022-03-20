package commands.instances;

import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import collection.DragonCollectionHandler;

/**
 * A command for filtering elements with age greater than specified. Invokes {@link DragonCollectionHandler#filterGreaterThanAge(String)}
 */
public class FilterGreaterThanAge extends AbstractCommand {
    private DragonCollectionHandler collectionHandler;

    public FilterGreaterThanAge(DragonCollectionHandler collectionHandler) {
        super("filter_greater_than_age", "вывести элементы, значение поля age которых больше заданного", CommandType.SIMPLE_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        String res = collectionHandler.filterGreaterThanAge(args.getArgs());
        return new ActionResult(true, res);
    }
}
