package collection;

import collection.classes.MainCollectible;
import collection.classes.MainCollectibleFactory;
import collection.history.CollectionHistoryHandler;
import collection.meta.CollectibleModel;
import collection.meta.CollectibleScheme;
import collection.storage.StorageHandler;
import exceptions.ElementIdException;
import exceptions.IncorrectCollectibleTypeException;
import exceptions.StorageException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.ServerHandler;
import utility.CollectionWithID;
import utility.PriorityQueueWithID;

import java.io.IOException;
import java.util.NoSuchElementException;

public class ServerCollectionHandler<T extends MainCollectible<T>> extends CollectionHandler<T> {
    @Getter
    private CollectionHistoryHandler<T> historyHandler;
    private final StorageHandler storageHandler;
    private final ServerHandler serverHandler;
    protected final MainCollectibleFactory<T> factory;
    private final Logger logger;

    public ServerCollectionHandler(StorageHandler storageHandler, ServerHandler serverHandler, MainCollectibleFactory<T> factory, Class<T> targetClass) {
        super(new PriorityQueueWithID<>(), targetClass);
        this.factory = factory;
        this.storageHandler = storageHandler;
        this.serverHandler = serverHandler;
        this.logger = LoggerFactory.getLogger("CollectionHandler");
    }

    public void add(CollectibleModel collectibleModel) throws StorageException, IncorrectCollectibleTypeException {
        long id = storageHandler.insert(collectibleModel);
        T collectible = factory.getObject(collectibleModel, id);
        collection.add(collectible);
        handleCollectionChange();
    }

    public void update(String arg, CollectibleModel collectibleModel) throws ElementIdException {
//        //TODO
//        long id = Long.parseLong(arg);
//        T newObject = factory.getObject(collectibleModel, id);
//        boolean result = collection.removeIf(dragon -> dragon.getId().equals(id));
//        if (result) {
//            collection.add(newObject);
//            handleCollectionChange();
//        } else {
//            throw new ElementIdException(arg);
//        }
    }

    public void save() throws IOException {
//        storageHandler.save(collection, targetClass);
    }

    public void load() {
        try {
            CollectionWithID<CollectibleModel> loadedCollection = this.storageHandler.load(collectibleScheme);
            this.collection.setId(loadedCollection.getId());
            for (CollectibleModel collectibleModel : loadedCollection) {
                this.collection.add(factory.getObject(collectibleModel));
            }
            logger.info("Successfully loaded collection");
            System.out.println(collection);
        } catch (StorageException | IncorrectCollectibleTypeException e) {
            logger.warn("Loaded collection is invalid, initializing an empty collection...(" + e.getMessage() + ")");
        } finally {
            historyHandler = new CollectionHistoryHandler<>(collection, serverHandler, targetClass);
        }
    }

    public void clear() {
        //TODO
        collection.clear();
        handleCollectionChange();
    }


    public void removeFirst() throws NoSuchElementException {
        //TODO
        this.collection.remove();
        handleCollectionChange();
    }

    public void removeById(String strId) throws ElementIdException, StorageException {
        //TODO
        try {
            long id = Long.parseLong(strId);
            storageHandler.removeById(id);
            if (!collection.removeIf(collectible -> collectible.getId().equals(id)))
                throw new ElementIdException(strId);
            else handleCollectionChange();
        } catch (NumberFormatException e) {
            throw new ElementIdException(strId);
        }

    }

    public void removeLower(CollectibleModel collectibleModel) {
        //TODO
        if (collection.removeIf(collectible -> collectible.compareTo(collectibleModel) < 0)) handleCollectionChange();
    }

    protected void handleCollectionChange() {
        collection.setId(storageHandler.getCollectionId());
        historyHandler.saveChange(collection);
        logger.info("Collection was changed");
    }


    public boolean isIdBehind(long id) {
        return collection.getId() - id > 0;
    }
}
