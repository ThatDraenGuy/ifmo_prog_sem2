package collection;

import annotations.LowerBounded;
import annotations.NotNull;
import annotations.CollectibleField;
import collection.meta.FieldData;
import exceptions.ValueNotValidException;

import java.lang.reflect.Field;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Validator {

    public static <T> T convertAndValidate(FieldData fieldData, Class<T> fieldType, String value) throws ValueNotValidException {
        T convertedValue = convert(fieldData.getSimpleName(), fieldType, value);
        return validate(fieldData, convertedValue);
    }

    public static <T> T validate(FieldData fieldData, T value) throws ValueNotValidException {
        if (fieldData.isNotNull() && value == null) {
            throw new ValueNotValidException(fieldData.getSimpleName() + " cannot be null!");
        }
        if (fieldData.isLowerBounded() && value != null) {
            double border = fieldData.getLowerBoundedValue();
            if (Double.parseDouble(value.toString()) <= border) {
                throw new ValueNotValidException(fieldData.getSimpleName() + " should be greater than " + border + ". Your input: " + value);
            }
        }
        return value;
    }

    /**
     * Converts a value from String to the needed type
     *
     * @param value a value to be converted
     * @return converted value
     * @throws ValueNotValidException if value couldn't be converted to fit the specified field
     */
    public static <T> T convert(String fieldName, Class<T> fieldType, String value) throws ValueNotValidException {
        if (value.equals("")) return null;
        HashMap<Class<?>, Function<String, ?>> variants = new HashMap<>();
        variants.put(Long.class, Long::parseLong);
        variants.put(long.class, Long::parseLong);
        variants.put(int.class, Integer::parseInt);
        variants.put(String.class, String::valueOf);
        variants.put(double.class, Double::parseDouble);
        variants.put(java.time.ZonedDateTime.class, java.time.ZonedDateTime::parse);

        Function<String, ?> convert = variants.get(fieldType);
        if (convert != null) {
            try {
                @SuppressWarnings({"unchecked"}) T convertedValue = (T) convert.apply(value);
                return convertedValue;
            } catch (NumberFormatException | DateTimeParseException e) {
                throw new ValueNotValidException(fieldName + " should have a " + fieldType.getSimpleName() + " value. Your value was: " + value);
            }
        }
        if (fieldType.isEnum()) {
            try {
                @SuppressWarnings({"unchecked", "rawtypes"}) T enumValue = (T) Enum.valueOf((Class<Enum>) fieldType, value);
                return enumValue;
            } catch (IllegalArgumentException e) {
                throw new ValueNotValidException(fieldName + " cannot have a value \"" + value + "\"");
            }
        }
        throw new ValueNotValidException(fieldName + " should have a " + fieldType.getSimpleName() + " value.. Your value was: " + value);

    }
}
