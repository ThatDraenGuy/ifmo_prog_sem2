package collection;

import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import collection.meta.CollectibleModel;
import collection.meta.CollectibleScheme;
import collection.meta.FieldModel;
import gui.Notifications;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.Getter;
import utility.ListAndId;

import java.util.Collection;
import java.util.Map;

public class ObservableCollection<T extends MainCollectible<?>> {
    @Getter
    private final Class<T> targetClass;
    @Getter
    private final CollectibleScheme collectibleScheme;
    private final ObservableList<T> collection = FXCollections.observableArrayList();
    @Getter
    private final FilteredList<T> items = new FilteredList<>(collection);
    private final ClientCollectionHandler<T> collectionHandler;

    public ObservableCollection(ClientCollectionHandler<T> collectionHandler) {
        this.collectionHandler = collectionHandler;
        targetClass = collectionHandler.getTargetClass();
        collectibleScheme = collectionHandler.getCollectibleScheme();
        Notifications.subscribe(CollectionClassesHandler.COLLECTION_SET_EVENT, this, this::set);
        Notifications.subscribe(CollectionClassesHandler.COLLECTION_CHANGE_EVENT, this, this::applyChange);
    }

    public void applyChange(String event) {
        CollectionChange<T> change = collectionHandler.getLastChange();
        Collection<T> added = change.getAddedElements();
        Collection<T> removed = change.getRemovedElements();
        collection.addAll(added);
        collection.removeAll(removed);
    }

    public void put(ListAndId<T> listAndId) {
        collection.addAll(listAndId.getList());
    }

    public void set(String event) {
        ListAndId<T> listAndId = collectionHandler.getCollection();
        collection.clear();
        put(listAndId);
    }

    public void setFilter(Map<String, String> filter) {
        items.setPredicate(t -> {
            CollectibleModel model = t.toModel();
            Map<String, FieldModel> values = model.getValues();
            for (String key : filter.keySet()) {
                if (!values.containsKey(key)) return false;
                FieldModel data = values.get(key);
                Object value = data.getValue();
                String str = value == null ? "" : value.toString();
                if (!str.equals(filter.get(key))) return false;
            }
            return true;
        });
    }
}

