package collection.meta;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class InputtedValue {
    @Getter
    private String string;
    @Getter
    private Map<String, InputtedValue> inputtedValues;
    @Getter
    private final boolean complex;

    public InputtedValue(Object input) {
        complex = input instanceof Map<?, ?>;
        if (complex) {
            inputtedValues = new HashMap<>();
            Map<String, ?> mappedInput = (Map<String, ?>) input;
            for (String key : mappedInput.keySet()) {
                inputtedValues.put(key, new InputtedValue(mappedInput.get(key)));
            }
        } else {
            string = input.toString();
        }
    }

    @Override
    public String toString() {
        //TODO think? (creation of inputted value from inputted value)
        return string;
    }
}
