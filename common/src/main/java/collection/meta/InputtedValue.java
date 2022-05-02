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
        if (input == null) {
            string = "";
            complex = false;
            return;
        }
        if (input instanceof InputtedValue inputtedValue) {
            this.string = inputtedValue.getString();
            this.inputtedValues = inputtedValue.getInputtedValues();
            this.complex = inputtedValue.isComplex();
            return;
        }
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
        if (complex) return inputtedValues.toString();
        else return string;
    }
}
