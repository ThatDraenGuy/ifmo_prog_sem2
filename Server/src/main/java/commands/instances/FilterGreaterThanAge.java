package commands.instances;

import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import collection.CollectionHandler;

/**
 * A command for filtering elements with age greater than specified. Invokes {@link CollectionHandler#filterGreaterThanAge(String)}
 */
public class FilterGreaterThanAge extends AbstractCommand {
    private CollectionHandler collectionHandler;

    public FilterGreaterThanAge(CollectionHandler collectionHandler) {
        super("filter_greater_than_age", "вывести элементы, значение поля age которых больше заданного", CommandType.SIMPLE_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        String res = collectionHandler.filterGreaterThanAge(args.getArgs());
        return new ActionResult(true, res);
    }
}
