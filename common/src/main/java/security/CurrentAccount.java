package security;

import lombok.Getter;
import lombok.Setter;

public class CurrentAccount {
    @Getter
    @Setter
    private static Account account;
}
