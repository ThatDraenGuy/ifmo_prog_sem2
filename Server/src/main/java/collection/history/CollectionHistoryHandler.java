package collection.history;

import collection.classes.MainCollectible;
import utility.PriorityQueueWithID;
import utility.QueueWithID;
import web.ServerHandler;
import utility.LimitedCollection;
import web.UserHandler;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionHistoryHandler<T extends MainCollectible<T>> {
    private final QueueWithID<T> currentCollection;
    private final LimitedCollection<CollectionChange<T>> changeHistory;
    private final ServerHandler serverHandler;
    private final Class<T> targetClass;

    public CollectionHistoryHandler(QueueWithID<T> currentCollection, ServerHandler serverHandler, Class<T> targetClass) {
        this.currentCollection = new PriorityQueueWithID<>();
        this.currentCollection.addAll(currentCollection);
        this.currentCollection.setId(currentCollection.getId());
        this.serverHandler = serverHandler;
        this.changeHistory = new LimitedCollection<>(10);
        this.targetClass = targetClass;
    }

    public void saveChange(QueueWithID<T> newCollection) {
        Collection<T> addedElements = newCollection.stream().filter(x -> !currentCollection.contains(x)).collect(Collectors.toCollection(ArrayList::new));
        Collection<T> removedElements = currentCollection.stream().filter(x -> !newCollection.contains(x)).collect(Collectors.toCollection(ArrayList::new));
        CollectionChange<T> change = new CollectionChange<>(addedElements, removedElements, newCollection.getId(), targetClass);
        changeHistory.add(change);
        currentCollection.clear();
        currentCollection.addAll(newCollection);
        currentCollection.setId(newCollection.getId());
        Deque<CollectionChange<? extends MainCollectible<?>>> changes = new ArrayDeque<>(1);
        changes.add(change);
        serverHandler.sendCollectionChanges(changes);
    }

    public void handleBehindId(UserHandler userHandler, long userCollectionId) {
        if (userCollectionId == 0) {
            userHandler.sendFullCollectionChangeRequest(currentCollection);
            return;
        }
        long lastSupportedId = currentCollection.getId() - changeHistory.size();
        if (userCollectionId < lastSupportedId) {
            userHandler.sendFullCollectionChangeRequest(currentCollection);
        } else {
            long changesNeeded = currentCollection.getId() - userCollectionId;
            long changesSkipped = changeHistory.size() - changesNeeded;
            Queue<CollectionChange<? extends MainCollectible<?>>> changes = changeHistory.stream().skip(changesSkipped).collect(Collectors.toCollection(PriorityQueue::new));
            userHandler.sendCollectionChangeRequest(changes);
        }
    }
}
