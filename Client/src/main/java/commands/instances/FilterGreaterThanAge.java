package commands.instances;

import collection.CollectionClassesHandler;
import collection.CollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandArgsType;

/**
 * A command for filtering elements with age greater than specified. Invokes {@link CollectionHandler#filterGreaterThanAge(String)}
 */
public class FilterGreaterThanAge extends AbstractCommand {
    private final CollectionClassesHandler collectionClassesHandler;

    public FilterGreaterThanAge(CollectionClassesHandler collectionClassesHandler) {
        super("filter_greater_than_age", "вывести элементы, значение поля age которых больше заданного", CommandArgsType.SIMPLE_ARG);
        this.collectionClassesHandler = collectionClassesHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        String res = collectionClassesHandler.getCurrentCollectionHandler().filterGreaterThanAge(args.getArgs());
        return new ActionResult(true, res);
    }
}
