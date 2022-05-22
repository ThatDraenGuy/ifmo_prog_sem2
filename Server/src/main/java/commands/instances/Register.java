package commands.instances;

import collection.meta.CollectibleModel;
import commands.*;
import exceptions.IncorrectAccountDataException;
import exceptions.IncorrectCollectibleTypeException;
import exceptions.StorageException;
import security.Account;
import security.AccountFactory;
import security.AccountsHandler;

public class Register extends AbstractCommand {
    private final AccountsHandler accountsHandler;
    private final AccountFactory accountFactory;

    public Register(AccountsHandler accountsHandler) {
        super("register", "command to register a new account", new CommandArgsInfo(CommandArgsType.COMPLEX_ARG, Account.class), CommandAccessLevel.GUEST);
        this.accountsHandler = accountsHandler;
        this.accountFactory = new AccountFactory();
    }

    @Override
    protected ActionResult action(ExecutionPayload executionPayload) {
        try {
            CollectibleModel model = executionPayload.getCommandArgs().getCollectibleModel();
            Account account = accountFactory.getObject(model);
            accountsHandler.register(account.getName(), account.getPassword());
            return new ActionResult(true, "registerSuccess");
        } catch (StorageException e) {
            return new ActionResult(false, "storageException");
        } catch (IncorrectAccountDataException | IncorrectCollectibleTypeException e) {
            return new ActionResult(false, e.getMessage());
        }
    }
}
