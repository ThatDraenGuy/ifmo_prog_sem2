package collection;

import collection.classes.Dragon;
import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import exceptions.CollectionVersionIsBehindException;
import lombok.Getter;
import utility.LimitedCollection;
import utility.QueueWithID;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

public class CollectionClassesHandler {
    @Getter
    volatile private ClientCollectionHandler<?> currentCollectionHandler;
    @Getter
    private Class<? extends MainCollectible<?>> currentClass;
    private final Map<Class<?>, CollectionSetupper<?>> supportedClasses;

    public CollectionClassesHandler() {
        supportedClasses = new HashMap<>();
        supportedClasses.put(Dragon.class, new DragonCollectionSetupper());
        currentCollectionHandler = null;
    }

    public void createCollection(Class<?> targetClass) {
        checkClassSupport(targetClass);
        currentCollectionHandler = supportedClasses.get(targetClass).setup();
        currentClass = (Class<? extends MainCollectible<?>>) targetClass;
//        System.out.println("created empty collection: " + currentCollectionHandler);
    }

    public void handleTargetClass(Class<?> targetClass) {
        if (targetClass == null) return;
        checkClassSupport(targetClass);
        if (!isCurrentClass(targetClass)) createCollection(targetClass);
    }

    public void createCollection(Class<?> targetClass, QueueWithID<? extends MainCollectible<?>> queue) {
        checkClassSupport(targetClass);
        currentCollectionHandler = supportedClasses.get(targetClass).setup(queue);
        currentClass = (Class<? extends MainCollectible<?>>) targetClass;
//        System.out.println("created collection from server collection: " + currentCollectionHandler);
    }

    public void applyCollectionChanges(Queue<CollectionChange<? extends MainCollectible<?>>> collectionChanges) {
        try {
            for (CollectionChange<? extends MainCollectible<?>> collectionChange : collectionChanges) {
                checkClassSupport(collectionChange.getElementClass());
                currentCollectionHandler.applyChange(collectionChange);
//                System.out.println("applied collectionChange: " + collectionChange);
            }
        } catch (CollectionVersionIsBehindException e) {
            e.printStackTrace();
        }
    }

    public void applyFullCollection(QueueWithID<? extends MainCollectible<?>> queue) {
        Optional<?> element = queue.stream().findAny();
        if (element.isPresent()) {
            Class<?> targetClass = element.get().getClass();
            checkClassSupport(targetClass);
            if (isCurrentClass(targetClass)) currentCollectionHandler.setCollection(queue);
            else createCollection(targetClass, queue);
        }

    }

    private void checkClassSupport(Class<?> targetClass) {
        if (!supportedClasses.containsKey(targetClass)) System.exit(1);
    }

    private boolean isCurrentClass(Class<?> targetClass) {
        if (currentClass == null) return false;
        return (currentClass.equals(targetClass));
    }

    public long getCurrentId() {
        if (currentCollectionHandler == null) return 0L;
        return currentCollectionHandler.getId();
    }

    public CollectionBuilder<?> getCurrentCollectionBuilder() {
        return currentCollectionHandler.getCollectionBuilder();
    }
}
