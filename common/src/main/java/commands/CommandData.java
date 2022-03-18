package commands;

import lombok.Getter;

import java.io.Serializable;

public class CommandData implements Serializable {
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final CommandType commandType;

    public CommandData(String name, String desc, CommandType type) {
        this.name = name;
        this.description = desc;
        this.commandType = type;
    }
}
