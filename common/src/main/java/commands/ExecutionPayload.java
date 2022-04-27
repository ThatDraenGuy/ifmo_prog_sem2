package commands;

import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import lombok.Getter;
import security.Account;
import security.CurrentAccount;
import utility.ListAndId;

import java.io.Serializable;
import java.util.Queue;

public class ExecutionPayload implements Serializable {
    @Getter
    private final CommandArgs commandArgs;
    @Getter
    private final Account account;
    @Getter
    private Queue<CollectionChange<? extends MainCollectible<?>>> collectionChanges;
    @Getter
    private ListAndId<? extends MainCollectible<?>> collection;


    public ExecutionPayload(CommandArgs commandArgs) {
        this.commandArgs = commandArgs;
        this.account = CurrentAccount.getAccount();
    }

    public ExecutionPayload(CommandArgs commandArgs, Account account) {
        this.commandArgs = commandArgs;
        this.account = account;
    }

    public ExecutionPayload(CommandArgs commandArgs, Account account, Queue<CollectionChange<? extends MainCollectible<?>>> collectionChanges) {
        this(commandArgs, account);
        this.collectionChanges = collectionChanges;
    }

    public ExecutionPayload(CommandArgs commandArgs, Account account, ListAndId<? extends MainCollectible<?>> collection) {
        this(commandArgs, account);
        this.collection = collection;
    }

    public ExecutionPayload(ExecutionPayload executionPayload) {
        this.commandArgs = executionPayload.commandArgs;
        this.account = executionPayload.account;
        this.collectionChanges = executionPayload.collectionChanges;
        this.collection = executionPayload.collection;
    }

}
