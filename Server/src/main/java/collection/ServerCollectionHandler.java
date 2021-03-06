package collection;

import collection.classes.MainCollectible;
import collection.classes.MainCollectibleFactory;
import collection.history.CollectionHistoryHandler;
import collection.meta.CollectibleModel;
import collection.storage.StorageHandler;
import exceptions.ElementIdException;
import exceptions.IncorrectCollectibleTypeException;
import exceptions.StorageException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utility.ListAndId;
import web.ServerHandler;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ServerCollectionHandler<T extends MainCollectible<T>> extends CollectionHandler<T> {
    @Getter
    private CollectionHistoryHandler<T> historyHandler;
    private final StorageHandler storageHandler;
    private final ServerHandler serverHandler;
    protected final MainCollectibleFactory<T> factory;
    private final Logger logger;

    public ServerCollectionHandler(StorageHandler storageHandler, ServerHandler serverHandler, MainCollectibleFactory<T> factory, Class<T> targetClass) {
        super(targetClass);
        this.factory = factory;
        this.storageHandler = storageHandler;
        this.serverHandler = serverHandler;
        this.logger = LoggerFactory.getLogger("CollectionHandler");
    }

    public synchronized void add(CollectibleModel collectibleModel) throws StorageException, IncorrectCollectibleTypeException {
        long id = storageHandler.insert(collectibleModel);
        T collectible = factory.getObject(collectibleModel, id);
        collection.getList().add(collectible);
        handleCollectionChange();
    }

    public synchronized void update(String arg, String owner, CollectibleModel collectibleModel) throws ElementIdException, StorageException, IncorrectCollectibleTypeException {
        long id = Long.parseLong(arg);
        if (!storageHandler.update(id, owner, collectibleModel)) throw new ElementIdException(arg);

        T newObject = factory.getObject(collectibleModel, id);
        collection.getList().removeIf(t -> t.getId().equals(id));
        collection.getList().add(newObject);
        handleCollectionChange();
    }

    public void save() throws IOException {
//        storageHandler.save(collection, targetClass);
    }

    public void load() {
        try {
            ListAndId<CollectibleModel> loadedCollection = this.storageHandler.load(collectibleScheme);
            this.collection.setId(loadedCollection.getId());
            for (CollectibleModel collectibleModel : loadedCollection.getList()) {
                this.collection.getList().add(factory.getObject(collectibleModel));
            }
            logger.info("Successfully loaded collection");
        } catch (StorageException | IncorrectCollectibleTypeException e) {
            logger.warn("Loaded collection is invalid, initializing an empty collection...(" + e.getMessage() + ")");
        } finally {
            historyHandler = new CollectionHistoryHandler<>(collection, serverHandler, targetClass);
        }
    }

    public synchronized void clear(String owner) throws StorageException {
        storageHandler.clear(owner);
        collection.getList().removeIf(x -> x.getOwner().equals(owner));
        handleCollectionChange();
    }


    public synchronized void removeFirst(String owner) throws NoSuchElementException, StorageException {
        Optional<T> element = this.collection.getList().stream().filter(collectible -> collectible.getOwner().equals(owner)).findFirst();
        if (element.isEmpty()) throw new NoSuchElementException();
        T collectible = element.get();
        long id = collectible.getId();
        storageHandler.removeById(id, owner);
        this.collection.getList().remove(collectible);
        handleCollectionChange();
    }

    public synchronized void removeById(String strId, String owner) throws ElementIdException, StorageException {
        try {
            long id = Long.parseLong(strId);
            boolean res = storageHandler.removeById(id, owner);
            if (!res) throw new ElementIdException(strId);
            collection.getList().removeIf(collectible -> collectible.getId().equals(id) && collectible.getOwner().equals(owner));
            handleCollectionChange();
        } catch (NumberFormatException e) {
            throw new ElementIdException(strId);
        }

    }

    public synchronized void removeLower(CollectibleModel collectibleModel, String owner) throws StorageException {
        List<Long> ids = collection.getList().stream().filter(collectible -> collectible.compareTo(collectibleModel) < 0 && collectible.getOwner().equals(owner))
                .map(MainCollectible::getId).collect(Collectors.toCollection(ArrayList::new));
        if (ids.size() > 0) {
            if (storageHandler.removeABunch(ids, owner)) {
                this.collection.getList().removeIf(collectible -> collectible.compareTo(collectibleModel) < 0 && collectible.getOwner().equals(owner));
                handleCollectionChange();
            }
        }
    }

    protected void handleCollectionChange() {
        collection.getId().incrementAndGet();
        historyHandler.saveChange(collection);
        logger.info("Collection was changed");
    }


    public synchronized boolean isIdBehind(long id) {
        return collection.getId().get() - id > 0;
    }
}
