package commands;

import lombok.Getter;
import lombok.Setter;


/**
 * Enum for a command's type.
 */
public enum CommandArgsType {
    NO_ARGS,
    SIMPLE_ARG,
    LONG_ARG,
    COMPLEX_ARG,
    BOTH_ARG;

    // for use with LONG_ARG
    @Getter
    private String[] argsNames;

    public CommandArgsType setArgsNames(String[] args) {
        this.argsNames = args;
        return this;
    }
}
