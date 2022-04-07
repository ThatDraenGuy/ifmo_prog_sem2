package commands;

import collection.classes.MainCollectible;
import collection.classes.RawCollectible;
import collection.history.CollectionChange;
import lombok.Getter;
import utility.LimitedCollection;
import utility.QueueWithID;

import java.io.Serializable;
import java.util.Queue;

/**
 * A class that represents arguments command needs to perform its action. As of now it uses separated fields for
 * simple args and complex args but in near future it might change to a single String[] field
 */
public class CommandArgs implements Serializable {
    @Getter
    private final String args;
    @Getter
    private RawCollectible<?> rawObject;
    @Getter
    private Queue<CollectionChange<? extends MainCollectible<?>>> collectionChanges;
    @Getter
    private QueueWithID<? extends MainCollectible<?>> fullCollection;

    public CommandArgs(String args) {
        this.args = args;
    }


    public CommandArgs(String args, RawCollectible<?> object) {
        this.rawObject = object;
        this.args = args;
    }

    public CommandArgs(String args, Queue<CollectionChange<? extends MainCollectible<?>>> collectionChanges) {
        this.args = args;
        this.collectionChanges = collectionChanges;
    }

    public CommandArgs(String args, QueueWithID<? extends MainCollectible<?>> fullCollection) {
        this.args = args;
        this.fullCollection = fullCollection;
    }
}
