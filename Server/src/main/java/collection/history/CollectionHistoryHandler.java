package collection.history;

import collection.classes.MainCollectible;
import utility.ListAndId;
import web.ServerHandler;
import utility.LimitedCollection;
import web.UserHandler;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionHistoryHandler<T extends MainCollectible<T>> {
    private final ListAndId<T> currentCollection;
    private final LimitedCollection<CollectionChange<T>> changeHistory;
    private final ServerHandler serverHandler;
    private final Class<T> targetClass;

    public CollectionHistoryHandler(ListAndId<T> currentCollection, ServerHandler serverHandler, Class<T> targetClass) {
        this.currentCollection = new ListAndId<>(targetClass);
        this.currentCollection.getList().addAll(currentCollection.getList());
        this.currentCollection.setId(currentCollection.getId());
        this.serverHandler = serverHandler;
        this.changeHistory = new LimitedCollection<>(10);
        this.targetClass = targetClass;
    }

    public void saveChange(ListAndId<T> newCollection) {
        Collection<T> addedElements = newCollection.getList().stream().filter(x -> !currentCollection.getList().contains(x)).collect(Collectors.toCollection(ArrayList::new));
        Collection<T> removedElements = currentCollection.getList().stream().filter(x -> !newCollection.getList().contains(x)).collect(Collectors.toCollection(ArrayList::new));
        CollectionChange<T> change = new CollectionChange<>(addedElements, removedElements, newCollection.getId(), targetClass);
        changeHistory.add(change);
        currentCollection.getList().clear();
        currentCollection.getList().addAll(newCollection.getList());
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
