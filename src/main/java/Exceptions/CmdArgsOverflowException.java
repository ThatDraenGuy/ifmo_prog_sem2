package Exceptions;

import cmd.CmdArgs;

public class CmdArgsOverflowException extends Exception {
    public CmdArgsOverflowException() {
        super ("Too many arguments for a command!");
    }
}
