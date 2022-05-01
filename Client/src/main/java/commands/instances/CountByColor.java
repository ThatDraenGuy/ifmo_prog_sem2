package commands.instances;

import collection.CollectionClassesHandler;
import commands.*;

/**
 * A command for counting elements with specified color.
 */
public class CountByColor extends AbstractCommand {
    private final CollectionClassesHandler collectionClassesHandler;

    public CountByColor(CollectionClassesHandler collectionClassesHandler) {
        super("count_by_color", "вывести количество элементов, значение поля color которых равно заданному", new CommandArgsInfo(CommandArgsType.SIMPLE_ARG));
        this.collectionClassesHandler = collectionClassesHandler;
    }

    @Override
    public ActionResult action(ExecutionPayload executionPayload) {
        CommandArgs args = executionPayload.getCommandArgs();
        try {
            long res = collectionClassesHandler.getCurrentCollectionHandler().countByColor(args.getArgs());
            return new ActionResult(true, "The result is " + res);
        } catch (IllegalArgumentException e) {
            return new ActionResult(false, "No such Color value: \"" + args.getArgs() + "\"");
        }
    }
}
