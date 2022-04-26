package security;

import commands.CommandAccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;


public class Account implements Serializable {
    @Getter
    private final String username;
    @Getter
    private final String password;
    @Getter
    private final CommandAccessLevel accessLevel;

    public Account(String username, String password, CommandAccessLevel accessLevel) {
        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", accessLevel=" + accessLevel +
                '}';
    }
}
