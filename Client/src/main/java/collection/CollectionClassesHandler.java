package collection;

import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import console.ConsoleHandler;
import exceptions.CollectionVersionIsBehindException;
import exceptions.IncorrectCollectibleTypeException;
import gui.Notifications;
import javafx.scene.control.TableView;
import lombok.Getter;
import utility.ListAndId;

import java.util.Queue;

public class CollectionClassesHandler {
    public final static String COLLECTIBLE_SCHEME_CHANGE_EVENT = "COLLECTIBLE_SCHEME_CHANGE_EVENT";
    @Getter
    volatile private ClientCollectionHandler<?> currentCollectionHandler;
    @Getter
    private Class<? extends MainCollectible<?>> currentClass;
    @Getter
    private final ConsoleHandler consoleHandler;

    public CollectionClassesHandler(ConsoleHandler consoleHandler) {
        currentCollectionHandler = null;
        this.consoleHandler = consoleHandler;
    }

    private <T extends MainCollectible<?>> void createCollection(Class<T> targetClass) {
        currentCollectionHandler = new ClientCollectionHandler<>(targetClass);
        handleCollectionCreation(targetClass);
        consoleHandler.debugMessage("created empty collection: " + currentCollectionHandler);
    }

    private <T extends MainCollectible<?>> void handleCollectionCreation(Class<T> targetClass) {
        currentClass = targetClass;
        Notifications.publish(COLLECTIBLE_SCHEME_CHANGE_EVENT);
    }

    public void handleTargetClass(Class<? extends MainCollectible<?>> targetClass) {
        if (targetClass == null) return;
        if (!isCurrentClass(targetClass)) createCollection(targetClass);
    }

    private <T extends MainCollectible<?>> void createCollection(Class<T> targetClass, ListAndId<T> listAndId) {
        currentCollectionHandler = new ClientCollectionHandler<>(targetClass, listAndId);
        handleCollectionCreation(targetClass);
        consoleHandler.debugMessage("created collection from server collection: " + currentCollectionHandler);
    }

    public void applyCollectionChanges(Queue<CollectionChange<? extends MainCollectible<?>>> collectionChanges) throws IncorrectCollectibleTypeException {
        try {
            for (CollectionChange<? extends MainCollectible<?>> collectionChange : collectionChanges) {
                currentCollectionHandler.applyChange(collectionChange);
                consoleHandler.debugMessage("applied collectionChange: " + collectionChange);
            }
        } catch (CollectionVersionIsBehindException e) {
            e.printStackTrace();
        }
    }

    public <T extends MainCollectible<?>> void applyFullCollection(ListAndId<T> listAndId) throws IncorrectCollectibleTypeException {
        Class<T> targetClass = listAndId.getTargetClass();
        if (isCurrentClass(targetClass)) {
            currentCollectionHandler.setCollection(listAndId);
        } else createCollection(targetClass, listAndId);
    }


    private boolean isCurrentClass(Class<?> targetClass) {
        if (currentClass == null) return false;
        return (currentClass.equals(targetClass));
    }

    public long getCurrentId() {
        if (currentCollectionHandler == null) return 0L;
        return currentCollectionHandler.getId();
    }
}
