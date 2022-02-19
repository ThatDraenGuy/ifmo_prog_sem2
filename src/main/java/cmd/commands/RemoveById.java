package cmd.commands;

import Collection.CollectionHandler;
import Exceptions.ElementIdException;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;
import cmd.AbstractCommand;

public class RemoveById extends AbstractCommand {
    CollectionHandler collectionHandler;
    public RemoveById(CollectionHandler collectionHandler) {
        super("remove_by_id", "удалить элемент из коллекции по его id", CmdType.SIMPLE_ARG);
        this.collectionHandler=collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        try {
            collectionHandler.removeById(args.getArgs());
            return new ActionResult(true, "Successfully deleted element with id "+args.getArgs());
        } catch (ElementIdException e) {
            return new ActionResult(false, e.getMessage());
        }
    }
}
