package commands.instances;

import commands.*;
import exceptions.IncorrectAccountDataException;
import exceptions.StorageException;
import security.AccountsHandler;

public class Register extends AbstractComplicatedCommand {
    private final AccountsHandler accountsHandler;

    public Register(AccountsHandler accountsHandler) {
        super("register", "command to register a new account", CommandArgsType.LONG_ARG.setArgsNames(new String[]{"login", "password"}), CommandAccessLevel.GUEST);
        this.accountsHandler = accountsHandler;
    }

    @Override
    protected ActionResult complicatedAction(ExecutionPayload executionPayload) {
        try {
            //TODO safe stuff?
            String login = executionPayload.getCommandArgs().getLongArgs()[0];
            String password = executionPayload.getCommandArgs().getLongArgs()[1];
            accountsHandler.register(login, password);
            return new ActionResult(true, "Successfully registered account");
        } catch (StorageException e) {
            return new ActionResult(false, "A storage exception has occurred");
        } catch (IncorrectAccountDataException e) {
            return new ActionResult(false, e.getMessage());
        }
    }
}
