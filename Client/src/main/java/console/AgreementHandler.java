package console;

import exceptions.ValueNotValidException;

import java.util.ArrayList;
import java.util.List;

public class AgreementHandler {
    private final List<String> yesValues;
    private final List<String> noValues;

    public AgreementHandler() {
        this.yesValues = yesValues();
        this.noValues = noValues();
    }

    public boolean getAgreement(String input) throws ValueNotValidException {
        if (yesValues.contains(input.toLowerCase())) return true;
        if (noValues.contains(input.toLowerCase())) return false;
        throw new ValueNotValidException("I dunno what this input is");
    }


    private List<String> yesValues() {
        String[] yes = {
                "yes",
                "y",
                "yeah",
                "ya",
                "sure",
                "да",
                "ага",
                "угу",
                "д"
        };
        return List.of(yes);
    }

    private List<String> noValues() {
        String[] no = {
                "no",
                "nah",
                "n",
                "nope",
                "нет",
                "не",
                "н",
                "неа"
        };
        return List.of(no);
    }
}
