package commands.instances;

import commands.*;
import security.Account;
import security.CurrentAccount;

public class SetAccount extends AbstractCommand {
    private final ExecutionController executionController;

    public SetAccount(ExecutionController executionController) {
        super("set_account", "internal command for changing client's account", new CommandArgsInfo(CommandArgsType.NO_ARGS), CommandAccessLevel.INTERNAL);
        this.executionController = executionController;
    }

    @Override
    protected ActionResult action(ExecutionPayload executionPayload) {
        //TODO safe stuff?
        Account newAccount = executionPayload.getAccount();
        CurrentAccount.setAccount(newAccount);
        executionController.setUserAccessLevel(newAccount.getAccessLevel());
        return new ActionResult(true, "changed account");
    }
}
