package commands.instances;

import collection.CollectionClassesHandler;
import collection.CollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandArgsType;

/**
 * A command to filter elements by type. Invokes {@link CollectionHandler#filterByType(String)}
 */
public class FilterByType extends AbstractCommand {
    private CollectionClassesHandler collectionClassesHandler;

    public FilterByType(CollectionClassesHandler collectionClassesHandler) {
        super("filter_by_type", "вывести элементы, значение поля type которых равно заданному", CommandArgsType.SIMPLE_ARG);
        this.collectionClassesHandler = collectionClassesHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            String res = collectionClassesHandler.getCurrentCollectionHandler().filterByType(args.getArgs());
            return new ActionResult(true, res);
        } catch (IllegalArgumentException e) {
            return new ActionResult(false, "No such DragonType value: \"" + args.getArgs() + "\"");
        }
    }
}
