package commands.instances;

import collection.CollectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandArgsType;

/**
 * A command for counting elements with specified color.
 */
public class CountByColor extends AbstractCommand {
    private final CollectionHandler<?> collectionHandler;

    public CountByColor(CollectionHandler<?> collectionHandler) {
        super("count_by_color", "вывести количество элементов, значение поля color которых равно заданному", CommandArgsType.SIMPLE_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            long res = collectionHandler.countByColor(args.getArgs());
            return new ActionResult(true, "The result is " + res);
        } catch (IllegalArgumentException e) {
            return new ActionResult(false, "No such Color value: \"" + args.getArgs() + "\"");
        }
    }
}
