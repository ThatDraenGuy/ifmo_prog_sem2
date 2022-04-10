package commands.instances;

import collection.CollectionClassesHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandArgsType;

/**
 * A command for counting elements with specified color.
 */
public class CountByColor extends AbstractCommand {
    private final CollectionClassesHandler collectionClassesHandler;

    public CountByColor(CollectionClassesHandler collectionClassesHandler) {
        super("count_by_color", "вывести количество элементов, значение поля color которых равно заданному", CommandArgsType.SIMPLE_ARG);
        this.collectionClassesHandler = collectionClassesHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            long res = collectionClassesHandler.getCurrentCollectionHandler().countByColor(args.getArgs());
            return new ActionResult(true, "The result is " + res);
        } catch (IllegalArgumentException e) {
            return new ActionResult(false, "No such Color value: \"" + args.getArgs() + "\"");
        }
    }
}
