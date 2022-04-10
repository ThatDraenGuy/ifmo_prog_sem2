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

    public CommandData(String name, String desc, CommandArgsType type) {
        this.name = name;
        this.description = desc;
        this.commandArgsType = type;
    }

    public CommandData(String name, String desc, CommandArgsType type, CommandAccessLevel accessLevel) {
        this(name, desc, type);
        this.accessLevel = accessLevel;
    }
}
