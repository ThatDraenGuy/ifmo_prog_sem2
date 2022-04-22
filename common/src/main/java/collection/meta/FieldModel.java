package collection.meta;

import collection.Validator;
import exceptions.ValueNotValidException;
import lombok.Getter;

import java.util.Map;

public class FieldModel {
    @Getter
    private final FieldData fieldData;
    @Getter
    private final Object value;

    public FieldModel(FieldData fieldData, InputtedValue value) throws ValueNotValidException {
        this.fieldData = fieldData;
        if (fieldData.isCollectible()) {
            if (!value.isComplex())
                throw new ValueNotValidException(fieldData.getSimpleName() + " should be a collectible!");
            Map<String, InputtedValue> mappedCollectible = value.getInputtedValues();
            this.value = new CollectibleModel(fieldData.getCollectibleScheme(), mappedCollectible);
        } else {
            if (value.isComplex())
                throw new ValueNotValidException(fieldData.getSimpleName() + " shouldn't be a collectible!");
            this.value = Validator.convertAndValidate(fieldData, fieldData.getType(), value.getString());
        }
    }

    public CollectibleModel getCollectibleModel() {
        if (fieldData.isCollectible()) return (CollectibleModel) value;
        return null;
    }
}
