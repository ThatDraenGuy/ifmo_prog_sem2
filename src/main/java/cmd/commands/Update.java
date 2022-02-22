package cmd.commands;

import Collection.CollectionHandler;
import Exceptions.ElementIdException;
import cmd.AbstractCommand;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;

import java.lang.reflect.InvocationTargetException;

public class Update extends AbstractCommand {
    private CollectionHandler collectionHandler;
    public Update(CollectionHandler collectionHandler) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному", CmdType.BOTH_ARG);
        this.collectionHandler=collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        try {
            collectionHandler.update(args.getArgs(), args.getDeconstructedObject());
            return new ActionResult(true, "Successfully updated element with id " + args.getArgs());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return new ActionResult(false, e.toString());
        } catch (ElementIdException e) {
            return new ActionResult(false, e.getMessage());
        }

    }
}