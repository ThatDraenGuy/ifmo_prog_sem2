package commands.instances;

import collection.CollectionClassesHandler;
import collection.CollectionHandler;
import commands.AbstractComplicatedCommand;
import commands.ActionResult;
import commands.CommandArgsType;
import commands.ExecutionPayload;

public class ShowMy extends AbstractComplicatedCommand {
    private final CollectionClassesHandler collectionClassesHandler;

    public ShowMy(CollectionClassesHandler collectionClassesHandler) {
        super("show_my", "вывести ваши элементы коллекции в строковом представлении", CommandArgsType.NO_ARGS);
        this.collectionClassesHandler = collectionClassesHandler;
    }

    @Override
    protected ActionResult complicatedAction(ExecutionPayload executionPayload) {
        String res = collectionClassesHandler.getCurrentCollectionHandler().filterByOwner(executionPayload.getAccount().getUsername());
        return new ActionResult(true, res);
    }
}
