package Console;

import Collection.CollectionHandler;
import cmd.CmdArgs;
import cmd.CmdHandler;
import cmd.Command;
import common.CmdRequest;
import common.CmdResponse;

import java.util.HashMap;
import java.util.Scanner;

public class ConsoleHandler {
    CollectionHandler collectionHandler;
    CmdHandler cmdHandler;
    public ConsoleHandler(CollectionHandler collectionHandler, CmdHandler cmdHandler) {
        this.collectionHandler = collectionHandler;
        this.cmdHandler = cmdHandler;
    }
    public void start() {
        loop();
    }
    public void loop() {
        String input = promptInput();
        CmdRequest cmdRequest = parseInput(input);
        CmdResponse response = cmdHandler.executeCmd(cmdRequest);
        loop();

    }
    public String promptInput() {
        System.out.println("input");
        //TODO Understand if we need a message here
        Scanner inputScanner = new Scanner(System.in);
        String input = inputScanner.nextLine();
        return input;
    }
    public CmdRequest parseInput(String input) {
        Command cmd;
        CmdArgs cmdArgs = null;
        if (input.contains(" ")) {
            String[] str = input.split(" ");
            input = str[0];
            cmdArgs = parseArgs(str[1]);
            if (str.length>2) {
                System.out.println("too many args?");
                //TODO Write an exception + check if this is true
            }
        }
        cmd = parseCommand(input);
        return (new CmdRequest(cmd, cmdArgs));
    }
    public Command parseCommand(String input) {
        if (cmdHandler.isInCmds(input)) {
            return cmdHandler.getCmds().get(input);
        } else {
            // TODO Write an exception
            System.out.println("No such command!");
            return null;
        }
    }
    public CmdArgs parseArgs(String input) {
        CmdArgs args = new CmdArgs(input);
        return args;
        // TODO think about this
    }

}
