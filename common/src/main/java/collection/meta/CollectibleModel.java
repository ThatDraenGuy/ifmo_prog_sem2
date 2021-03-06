package collection.meta;

import exceptions.ValueNotValidException;
import lombok.Getter;
import security.CurrentAccount;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class CollectibleModel implements Serializable, Comparable<CollectibleModel> {
    @Getter
    private final CollectibleScheme collectibleScheme;
    @Getter
    private final Map<String, FieldModel> values;
    @Getter
    private boolean completed;

    public CollectibleModel(CollectibleScheme collectibleScheme) {
        this.collectibleScheme = collectibleScheme;
        this.values = new HashMap<>();
        this.completed = false;
    }

    public CollectibleModel(CollectibleScheme collectibleScheme, Map<String, InputtedValue> collectible) throws ValueNotValidException {
        this(collectibleScheme);
        put(collectible);
    }

    public void put(String field, InputtedValue value) throws ValueNotValidException {
        if (!collectibleScheme.getFieldsData().containsKey(field))
            throw new ValueNotValidException("No such field: " + field);
        FieldData fieldData = collectibleScheme.getFieldsData().get(field);
        if (!values.containsKey(field)) {
            values.put(field, new FieldModel(fieldData, value));
        }
    }

    private void putIfPossible(String field, InputtedValue value) throws ValueNotValidException {
        if (collectibleScheme.getFieldsData().containsKey(field)) {
            FieldData fieldData = collectibleScheme.getFieldsData().get(field);
            if (!values.containsKey(field)) {
                values.put(field, new FieldModel(fieldData, value));
            }
        }
    }

    public void put(Map<String, InputtedValue> values) throws ValueNotValidException {
        for (String field : values.keySet()) {
            put(field, values.get(field));
        }
        complete();
    }

    public void complete() throws ValueNotValidException {
        putIfPossible("creationDate", new InputtedValue(ZonedDateTime.now()));
        putIfPossible("owner", new InputtedValue(CurrentAccount.getAccount().getName()));
        for (String field : collectibleScheme.getFieldsData().keySet()) {
            if (collectibleScheme.getFieldsData().get(field).isNotNull() && !values.containsKey(field) && !field.equals("id"))
                throw new ValueNotValidException(field + " cannot be null!");
            if (!collectibleScheme.getFieldsData().get(field).isNotNull()) put(field, new InputtedValue(null));
        }
        this.completed = true;
    }

    @Override
    public String toString() {
        return values.toString();
    }

    @Override
    public int compareTo(CollectibleModel c) {
        return this.values.keySet().size() - c.values.keySet().size();
    }
}
