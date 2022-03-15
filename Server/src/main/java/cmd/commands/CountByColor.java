package cmd.commands;

import cmd.AbstractCommand;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;
import collection.CollectionHandler;

/**
 * A command for counting elements with specified color. Invokes {@link CollectionHandler#countByColor(String)}
 */
public class CountByColor extends AbstractCommand {
    private CollectionHandler collectionHandler;

    public CountByColor(CollectionHandler collectionHandler) {
        super("count_by_color", "вывести количество элементов, значение поля color которых равно заданному", CmdType.SIMPLE_ARG);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        try {
            int res = collectionHandler.countByColor(args.getArgs());
            return new ActionResult(true, "The result is " + res);
        } catch (IllegalArgumentException e) {
            return new ActionResult(false, "No such Color value: \"" + args.getArgs() + "\"");
        }
    }
}
