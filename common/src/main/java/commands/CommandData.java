package commands;

import lombok.Getter;

import java.io.Serializable;

public class CommandData implements Serializable {
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final CommandArgsType commandArgsType;
    @Getter
    private String[] argsNames;
    @Getter
    private final CommandAccessLevel accessLevel;

    public CommandData(String name, String desc, CommandArgsType type) {
        this.name = name;
        this.description = desc;
        this.commandArgsType = type;
        checkType();
        this.accessLevel = CommandAccessLevel.USER;
    }

    public CommandData(String name, String desc, CommandArgsType type, CommandAccessLevel accessLevel) {
        this.name = name;
        this.description = desc;
        this.commandArgsType = type;
        checkType();
        this.accessLevel = accessLevel;
    }

    private void checkType() {
        if (commandArgsType.equals(CommandArgsType.LONG_ARG)) argsNames = commandArgsType.getArgsNames();
    }

}
