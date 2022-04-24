package collection.meta;

import collection.classes.Collectible;
import exceptions.ValueNotValidException;
import lombok.Getter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class CollectibleModel implements Serializable {
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
            System.out.println("Inputting " + field + " as " + value);
            values.put(field, new FieldModel(fieldData, value));
        }
    }

    public void put(Map<String, InputtedValue> values) throws ValueNotValidException {
        for (String field : values.keySet()) {
            put(field, values.get(field));
        }
        complete();
    }

    public void complete() throws ValueNotValidException {
        put("creationDate", new InputtedValue(ZonedDateTime.now()));
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
        //TODO ?
    }
}
