package collection;

import annotations.LowerBounded;
import annotations.NotNull;
import exceptions.ValueNotValidException;

import java.lang.reflect.Field;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.function.Function;

public class Validator {
    /**
     *
     * @param deconstructedObject an object to be verified
     * @return validated and converted deconstructed version of an object
     * @throws ValueNotValidException if validation was failed
     */
//    public static HashMap<Field, Object> validate(HashMap<Field, Object> deconstructedObject) throws ValueNotValidException {
//        HashMap<Field, Object> validatedObject = new HashMap<>();
//        for (Field field : deconstructedObject.keySet()) {
//            //TODO Collectible?
//            if (field.isAnnotationPresent(UserAccessibleObject.class) && !deconstructedObject.get(field).equals("")) {
//                validatedObject.put(field, validate((HashMap<Field, Object>) deconstructedObject.get(field)));
//            } else {
//                validatedObject.put(field, validate(field, field.getType(), deconstructedObject.get(field).toString()));
//            }
//        }
//        return validatedObject;
//    }

    /**
     * @param field a field that inputted value will go in
     * @param value a value to be validated
     * @return validated and converted value
     * @throws ValueNotValidException if validation was failed
     */
    public static <T> T validate(Field field, Class<T> fieldType, String value) throws ValueNotValidException {
        T convertedValue = convert(field.getName(), fieldType, value);
        if (field.isAnnotationPresent(NotNull.class) && convertedValue == null) {
            throw new ValueNotValidException(field.getName() + " cannot be null!");
        }
        if (field.isAnnotationPresent(LowerBounded.class) && convertedValue != null) {
            double border = field.getAnnotation(LowerBounded.class).value();
            if (Double.parseDouble(value) <= border) {
                throw new ValueNotValidException(field.getName() + " should be greater than " + border + ". Your input: " + value);
            }
        }
        return convertedValue;
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
        throw new ValueNotValidException(fieldName + " should have a " + fieldType.getSimpleName() + " value. Your value was: " + value);

    }
}
