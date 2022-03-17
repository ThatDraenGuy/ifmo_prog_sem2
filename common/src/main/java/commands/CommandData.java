package commands;

import lombok.Getter;

import java.io.Serializable;

public class CommandData implements Serializable {
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private CommandType commandType;

    public CommandData(String name, String desc, CommandType type) {
        this.name = name;
        this.description = desc;
        this.commandType = type;
    }
}
