package commands.instances;

import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import collection.CollectionHandler;

/**
 * A command for counting elements with specified color. Invokes {@link CollectionHandler#countByColor(String)}
 */
public class CountByColor extends AbstractCommand {
    private CollectionHandler collectionHandler;

    public CountByColor(CollectionHandler collectionHandler) {
        super("count_by_color", "вывести количество элементов, значение поля color которых равно заданному", CommandType.SIMPLE_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            int res = collectionHandler.countByColor(args.getArgs());
            return new ActionResult(true, "The result is " + res);
        } catch (IllegalArgumentException e) {
            return new ActionResult(false, "No such Color value: \"" + args.getArgs() + "\"");
        }
    }
}
