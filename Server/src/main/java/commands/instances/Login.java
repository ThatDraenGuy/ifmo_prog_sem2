package commands.instances;

import collection.meta.CollectibleModel;
import collection.meta.CollectibleScheme;
import commands.*;
import exceptions.*;
import security.Account;
import security.AccountFactory;
import security.AccountsHandler;

public class Login extends AbstractCommand {
    private final AccountsHandler accountsHandler;
    private final AccountFactory accountFactory;

    public Login(AccountsHandler accountsHandler) {
        super("login", "log into your account", new CommandArgsInfo(CommandArgsType.COMPLEX_ARG, Account.class), CommandAccessLevel.GUEST);
        this.accountsHandler = accountsHandler;
        this.accountFactory = new AccountFactory();
    }

    @Override
    protected ActionResult action(ExecutionPayload executionPayload) {
        try {
            CollectibleModel model = executionPayload.getCommandArgs().getCollectibleModel();
            Account account = accountFactory.getObject(model);
            Account loggedAccount = accountsHandler.validate(account);
            if (executionPayload instanceof ServerExecutionPayload serverExecutionPayload) {
                serverExecutionPayload.getUserHandler().sendAccountChangeRequest(loggedAccount);
                return new ActionResult(true, "loginSuccess");
            } else return new ActionResult(false, "loginFailure");
        } catch (IncorrectAccountDataException | InvalidAccessLevelException | UnknownAccountException e) {
            return new ActionResult(false, e.getMessage());
        } catch (StorageException e) {
            return new ActionResult(false, "storageException");
        } catch (IncorrectCollectibleTypeException e) {
            return new ActionResult(false, "loginFailure");
        }
    }
}
