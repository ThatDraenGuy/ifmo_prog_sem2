package commands;

import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import commands.instances.FetchServerData;
import exceptions.InvalidAccessLevelException;
import lombok.Getter;
import lombok.Setter;
import message.*;
import security.Account;
import security.AccountsHandler;
import utility.ListAndId;
import web.UserHandler;

import java.util.Queue;

public class ServerCommandsHandler extends CommandsHandler {
    @Getter
    @Setter
    private ServerData serverData;
    private final AccountsHandler accountsHandler;
    @Getter
    private final Command fetchServerDataCommand = new FetchServerData();


    public ServerCommandsHandler(AccountsHandler accountsHandler) {
        super();
        this.accountsHandler = accountsHandler;
    }


    @Override
    protected Response createResponse(ActionResult actionResult) {
        return new CommandResponse(actionResult, serverData);
    }

    public Response executeCommand(Request request, UserHandler userHandler) {
        try {
            accountsHandler.validate(request.getExecutionPayload().getAccount());
            if (request.getExecutionPayload().getAccount().getAccessLevel().compareTo(request.getCommandData().getAccessLevel()) < 0)
                throw new InvalidAccessLevelException("You don't have access to this command");
            //TODO think?
        } catch (Exception e) {
            return createResponse(new ActionResult(false, e.getMessage()));
            //TODO think?
        }
        return super.execute(request.getCommandData(), new ServerExecutionPayload(request.getExecutionPayload(), userHandler));
    }


    public Request formulateFetchServerDataRequest(Account account) {
        return new CommandRequest(fetchServerDataCommand.getData(), new ExecutionPayload(new CommandArgs(), account), serverData);
    }

    public Request formulateDisconnectRequest(UserData userData, Account account) {
        return new CommandRequest(userData.getDisconnectCommandData(), new ExecutionPayload(new CommandArgs(), account), serverData);
    }

    public Request formulateCollectionChangeRequest(UserData userData, Account account, Queue<CollectionChange<? extends MainCollectible<?>>> collectionChanges) {
        return new CommandRequest(userData.getApplyCollectionChangeCommandData(), new ExecutionPayload(new CommandArgs(), account, collectionChanges));
    }

    public Request formulateFullCollectionChangeRequest(UserData userData, Account account, ListAndId<? extends MainCollectible<?>> collection) {
        return new CommandRequest(userData.getApplyFullCollectionCommandData(), new ExecutionPayload(new CommandArgs(), account, collection));
    }

    public Request formulateSetAccountRequest(UserData userData, Account account) {
        return new CommandRequest(userData.getSetAccountCommandData(), new ExecutionPayload(new CommandArgs(), account));
    }

}
