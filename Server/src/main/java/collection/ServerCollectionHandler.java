package collection;

import collection.classes.MainCollectible;
import collection.classes.MainCollectibleFactory;
import collection.classes.RawCollectible;
import collection.history.CollectionHistoryHandler;
import exceptions.ElementIdException;
import exceptions.InvalidCollectionException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.ServerHandler;
import utility.CollectionWithID;
import utility.PriorityQueueWithID;

import java.io.IOException;
import java.util.Map;

public class ServerCollectionHandler<T extends MainCollectible<T>> extends CollectionHandler<T> {
    @Getter
    private CollectionHistoryHandler<T> historyHandler;
    private final StorageHandler storageHandler;
    private final ServerHandler serverHandler;
    protected final MainCollectibleFactory<T> factory;
    private final Logger logger;

    public ServerCollectionHandler(StorageHandler storageHandler, ServerHandler serverHandler, MainCollectibleFactory<T> factory, CollectionBuilder<T> collectionBuilder, Class<T> targetClass) {
        super(new PriorityQueueWithID<>(), collectionBuilder, targetClass);
        this.factory = factory;
        this.storageHandler = storageHandler;
        this.serverHandler = serverHandler;
        this.logger = LoggerFactory.getLogger("CollectionHandler");
    }

    public void add(RawCollectible<T> rawObject) {
        collection.add(handleRawCollectible(rawObject));
        handleCollectionChange();
    }

    public void update(String arg, RawCollectible<T> rawCollectible) throws ElementIdException {
        long id = Long.parseLong(arg);
        T newObject = handleRawCollectible(rawCollectible, id);
        boolean result = collection.removeIf(dragon -> dragon.getId().equals(id));
        if (result) {
            collection.add(newObject);
            handleCollectionChange();
        } else {
            throw new ElementIdException(arg);
        }
    }

    public void save() throws IOException {
        storageHandler.save(collection, targetClass);
    }

    public void load() {
        try {
            CollectionWithID<Map<String, Object>> loadedCollection = this.storageHandler.load(targetClass);
            CollectionWithID<T> collection = collectionBuilder.build(loadedCollection);
            long id = checkIds(collection);
            this.collection.addAll(collection);
            this.collection.setId(collection.getId());
            factory.setNextId(id + 1);
        } catch (InvalidCollectionException e) {
            System.out.println("Loaded collection is invalid, initializing an empty collection...(" + e.getMessage() + ")");
        } finally {
            historyHandler = new CollectionHistoryHandler<>(collection, serverHandler, targetClass);
        }
    }

    @Override
    protected void handleCollectionChange() {
        collection.incrementId();
        historyHandler.saveChange(collection);
        logger.info("Collection was changed");
    }

    private T handleRawCollectible(RawCollectible<T> rawCollectible) {
        return factory.getObject(rawCollectible);
    }

    private T handleRawCollectible(RawCollectible<T> rawCollectible, long id) {
        return factory.getObject(rawCollectible, id);
    }

    public boolean isIdBehind(long id) {
        return collection.getId() - id > 0;
    }
}
