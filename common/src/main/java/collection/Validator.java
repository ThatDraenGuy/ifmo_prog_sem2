package collection;

import annotations.LowerBounded;
import annotations.NotNull;
import annotations.UserAccessibleObject;
import exceptions.ValueNotValidException;

import java.lang.reflect.Field;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.function.Function;

public class Validator {
    /**
     * Method for validating and converting a deconstructed version of an object. Invokes {@link #validate(Field, String)} for each field
     *
     * @param deconstructedObject an object to be verified
     * @return validated and converted deconstructed version of an object
     * @throws ValueNotValidException if validation was failed
     */
    public static HashMap<Field, Object> validate(HashMap<Field, Object> deconstructedObject) throws ValueNotValidException {
        HashMap<Field, Object> validatedObject = new HashMap<>();
        for (Field field : deconstructedObject.keySet()) {
            //TODO Collectible?
            if (field.isAnnotationPresent(UserAccessibleObject.class) && !deconstructedObject.get(field).equals("")) {
                validatedObject.put(field, validate((HashMap<Field, Object>) deconstructedObject.get(field)));
            } else {
                validatedObject.put(field, validate(field, deconstructedObject.get(field).toString()));
            }
        }
        return validatedObject;
    }

    /**
     * Validates and converts a field. Invokes {@link #convert(Field, String)}
     *
     * @param field a field that inputted value will go in
     * @param value a value to be validated
     * @return validated and converted value
     * @throws ValueNotValidException if validation was failed
     */
    public static Object validate(Field field, String value) throws ValueNotValidException {
        Object convertedValue = convert(field, value);
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
     * @param field a field that value will go in
     * @param value a value to be converted
     * @return converted value
     * @throws ValueNotValidException if value couldn't be converted to fit the specified field
     */
    public static Object convert(Field field, String value) throws ValueNotValidException {
        if (value.equals("")) return null;

        HashMap<Class<?>, Function<String, ?>> variants = new HashMap<>();
        variants.put(Long.class, Long::parseLong);
        variants.put(long.class, Long::parseLong);
        variants.put(int.class, Integer::parseInt);
        variants.put(String.class, String::valueOf);
        variants.put(double.class, Double::parseDouble);
        variants.put(java.time.ZonedDateTime.class, java.time.ZonedDateTime::parse);

        Function<String, ?> convert = variants.get(field.getType());
        if (convert != null) {
            try {
                return convert.apply(value);
            } catch (NumberFormatException | DateTimeParseException e) {
                throw new ValueNotValidException(field.getName() + " should have a " + field.getType().getSimpleName() + " value. Your value was: " + value);
            }
        }
        if (field.getType().isEnum()) {
            try {
                @SuppressWarnings({"unchecked", "rawtypes"}) Object enumValue = Enum.valueOf((Class<Enum>) field.getType(), value);
                return enumValue;
            } catch (IllegalArgumentException e) {
                throw new ValueNotValidException(field.getName() + " cannot have a value \"" + value + "\"");
            }
        }
        throw new ValueNotValidException(field.getName() + " should have a " + field.getType().getSimpleName() + " value. Your value was: " + value);

    }
}
