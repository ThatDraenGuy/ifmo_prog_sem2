package Console;

import Collection.Classes.Dragon;
import Collection.CollectionHandler;
import Exceptions.CmdArgsAmountException;
import Exceptions.CommandExecutionException;
import Exceptions.CommandNonExistentException;
import cmd.*;
import common.CmdRequest;
import common.CmdResponse;
import common.Request;
import common.Response;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
                Request cmdRequest = parseInput(input);
                Response response = executeCmd(cmdRequest);
                handleResponse(response);
            } catch (CommandNonExistentException | CmdArgsAmountException | CommandExecutionException e) {
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
    public String promptInput(String inputName) {
        System.out.println("Input "+inputName+":");
        Scanner inputScanner = new Scanner(System.in);
        return inputScanner.nextLine();
    }
    public Request parseInput(String input) throws CommandNonExistentException, CmdArgsAmountException {
        Command cmd;
        CmdArgs cmdArgs = new CmdArgs("");
        if (input.contains(" ")) {
            String[] str = input.split(" ");
            input = str[0];
            cmdArgs = parseArgs(str[1]);
            if (str.length>2) {
                throw new CmdArgsAmountException();
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
    public Response executeCmd(Request request) throws CmdArgsAmountException {
        CmdType type = request.getCmd().getCmdType();
        return switch (type) {
            case NO_ARGS, SIMPLE_ARG -> cmdHandler.executeCmd(request);
            case COMPLEX_ARG -> promptComplexArgs(request);
        };
    }
    public CmdResponse promptComplexArgs(Request request) throws CmdArgsAmountException {
        Field[] dragonFields = Dragon.class.getDeclaredFields();
        ArrayList<String> fieldNames = new ArrayList<>();
        for (Field dragonField : dragonFields) {
            fieldNames.add(dragonField.getName());
        }
        String args="";
        for (String fieldName : fieldNames) {
            args+=promptInput(fieldName)+" ";
        }
        request.setCmdArgs(new CmdArgs(args));
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
