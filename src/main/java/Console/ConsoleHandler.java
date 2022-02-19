package Console;

import Collection.CollectionHandler;
import Exceptions.CmdArgsOverflowException;
import Exceptions.CommandExecutionException;
import Exceptions.CommandNonExistentException;
import cmd.*;
import common.CmdRequest;
import common.CmdResponse;
import common.Request;
import common.Response;

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
        while (true) {
            try {
                String input = promptInput();
                CmdRequest cmdRequest = parseInput(input);
                CmdResponse response = executeCmd(cmdRequest);
                handleResponse(response);
            } catch (CommandNonExistentException | CmdArgsOverflowException | CommandExecutionException e) {
                errorMessage(e);
                continue;
            }

        }
    }
    public String promptInput() {
        System.out.println("Awaiting your input: ");
        Scanner inputScanner = new Scanner(System.in);
        return inputScanner.nextLine();
    }
    public CmdRequest parseInput(String input) throws CommandNonExistentException, CmdArgsOverflowException {
        Command cmd;
        CmdArgs cmdArgs = null;
        if (input.contains(" ")) {
            String[] str = input.split(" ");
            input = str[0];
            cmdArgs = parseArgs(str[1]);
            if (str.length>2) {
                throw new CmdArgsOverflowException();
            }
        }
        cmd = parseCommand(input);
        return (new CmdRequest(cmd, cmdArgs));
    }
    public Command parseCommand(String input) throws CommandNonExistentException {
        if (cmdHandler.isInCmds(input)) {
            return cmdHandler.getCmds().get(input);
        } else {
            throw new CommandNonExistentException(input);
        }
    }
    public CmdArgs parseArgs(String input) {
        CmdArgs args = new CmdArgs(input);
        return args;
        // TODO think about this
    }
    public CmdResponse executeCmd(Request request) {
        CmdType type = request.getCmd().getCmdType();
        return switch (type) {
            case NO_ARGS, SIMPLE_ARG -> cmdHandler.executeCmd(request);
            case COMPLEX_ARG -> promptComplexArgs(request);
        };
    }
    public CmdResponse promptComplexArgs(Request request) {

        //TODO implement
        return cmdHandler.executeCmd(request);
    }
    public void handleResponse(Response response) throws CommandExecutionException {
        ActionResult result = response.getActionResult();
        boolean isSuccess = result.isSuccess();
        if (isSuccess) {
            System.out.println(result.getMessage());
        } else {
            throw new CommandExecutionException(result);
        }
    }
    public void errorMessage(Exception e) {
        System.err.println(e.getMessage());
    }
}
