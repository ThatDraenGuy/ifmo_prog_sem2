package cmd.commands;

import collection.CollectionHandler;
import cmd.AbstractCommand;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;

public class FilterGreaterThanAge extends AbstractCommand {
    private CollectionHandler collectionHandler;
    public FilterGreaterThanAge(CollectionHandler collectionHandler) {
        super("filter_greater_than_age", "вывести элементы, значение поля age которых больше заданного", CmdType.SIMPLE_ARG);
        this.collectionHandler=collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        String res = collectionHandler.filterGreaterThanAge(args.getArgs());
        return new ActionResult(true, res);
    }
}
