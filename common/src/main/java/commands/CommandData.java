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
    private CommandAccessLevel accessLevel = CommandAccessLevel.USER;
    @Getter
    private boolean disconnectionNeeded = false;

    public CommandData(String name, String desc, CommandArgsType type) {
        this.name = name;
        this.description = desc;
        this.commandArgsType = type;
    }

    public CommandData(String name, String desc, CommandArgsType type, boolean disconnectionFlag) {
        this(name, desc, type);
        this.disconnectionNeeded = disconnectionFlag;
    }

    public CommandData(String name, String desc, CommandArgsType type, CommandAccessLevel accessLevel) {
        this(name, desc, type);
        this.accessLevel = accessLevel;
    }

    public CommandData(String name, String desc, CommandArgsType type, CommandAccessLevel accessLevel, boolean disconnectionFlag) {
        this(name, desc, type, disconnectionFlag);
        this.accessLevel = accessLevel;
    }
}
