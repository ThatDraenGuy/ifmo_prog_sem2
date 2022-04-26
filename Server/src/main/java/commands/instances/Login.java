package commands.instances;

import commands.*;
import exceptions.IncorrectAccountDataException;
import exceptions.StorageException;
import exceptions.UnknownAccountException;
import security.Account;
import security.AccountsHandler;

public class Login extends AbstractComplicatedCommand {
    private final AccountsHandler accountsHandler;

    public Login(AccountsHandler accountsHandler) {
        super("login", "log into your account", CommandArgsType.LONG_ARG.setArgsNames(new String[]{"login", "password"}), CommandAccessLevel.GUEST);
        this.accountsHandler = accountsHandler;
    }

    @Override
    protected ActionResult complicatedAction(ExecutionPayload executionPayload) {
        CommandArgs args = executionPayload.getCommandArgs();
        //TODO safe stuff?
        try {
            String[] longArgs = args.getLongArgs();
            String username = longArgs[0];
            String password = longArgs[1];
            Account loggedAccount = accountsHandler.validate(username, password);
            if (executionPayload instanceof ServerExecutionPayload serverExecutionPayload) {
                serverExecutionPayload.getUserHandler().sendAccountChangeRequest(loggedAccount);
                return new ActionResult(true, "Successfully logged into account");
            } else return new ActionResult(false, "An exception occurred");
        } catch (UnknownAccountException | StorageException | IncorrectAccountDataException e) {
            return new ActionResult(false, e.getMessage());
        }
    }
}
