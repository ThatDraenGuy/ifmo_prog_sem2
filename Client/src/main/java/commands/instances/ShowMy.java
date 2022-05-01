package commands.instances;

import collection.CollectionClassesHandler;
import commands.*;

public class ShowMy extends AbstractCommand {
    private final CollectionClassesHandler collectionClassesHandler;

    public ShowMy(CollectionClassesHandler collectionClassesHandler) {
        super("show_my", "вывести ваши элементы коллекции в строковом представлении", new CommandArgsInfo(CommandArgsType.NO_ARGS));
        this.collectionClassesHandler = collectionClassesHandler;
    }

    @Override
    protected ActionResult action(ExecutionPayload executionPayload) {
        String res = collectionClassesHandler.getCurrentCollectionHandler().filterByOwner(executionPayload.getAccount().getName());
        return new ActionResult(true, res);
    }
}
