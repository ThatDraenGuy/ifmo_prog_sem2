package commands.instances;

import collection.CollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandArgsType;

/**
 * A command for filtering elements with age greater than specified. Invokes {@link DragonCollectionHandler#filterGreaterThanAge(String)}
 */
public class FilterGreaterThanAge extends AbstractCommand {
    private CollectionHandler<?> collectionHandler;

    public FilterGreaterThanAge(CollectionHandler<?> collectionHandler) {
        super("filter_greater_than_age", "вывести элементы, значение поля age которых больше заданного", CommandArgsType.SIMPLE_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        String res = collectionHandler.filterGreaterThanAge(args.getArgs());
        return new ActionResult(true, res);
    }
}
