package cmd.commands;

import cmd.AbstractCommand;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;
import collection.CollectionHandler;

/**
 * A command to filter elements by type. Invokes {@link CollectionHandler#filterByType(String)}
 */
public class FilterByType extends AbstractCommand {
    private CollectionHandler collectionHandler;

    public FilterByType(CollectionHandler collectionHandler) {
        super("filter_by_type", "вывести элементы, значение поля type которых равно заданному", CmdType.SIMPLE_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        try {
            String res = collectionHandler.filterByType(args.getArgs());
            return new ActionResult(true, res);
        } catch (IllegalArgumentException e) {
            return new ActionResult(false, "No such DragonType value: \'" + args.getArgs() + "\"");
        }
    }
}
