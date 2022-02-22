package Console;

import Collection.CollectionHandler;
import Exceptions.CmdArgsAmountException;
import Exceptions.CommandExecutionException;
import Exceptions.CommandNonExistentException;
import Exceptions.ValueNotValidException;
import annotations.UserAccessibleEnum;
import annotations.UserAccessibleField;
import annotations.UserAccessibleObject;
import cmd.*;
import common.CmdRequest;
import common.Request;
import common.Response;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Scanner;

public class ConsoleHandler {
    private CollectionHandler collectionHandler;
    private CmdHandler cmdHandler;
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
                String input = promptInput("");
                Request cmdRequest = parseInput(input);
                Response response = executeCmd(cmdRequest);
                handleResponse(response);
            } catch (CommandNonExistentException | CmdArgsAmountException | CommandExecutionException e) {
                errorMessage(e);
            } catch (ClassNotFoundException e) {
                errorMessage(new Exception("A critical exception!"));
                //TODO ???
            }

        }
    }
    public String promptInput(String message) {
        if (message.equals("")) {
            System.out.println("Awaiting your input: ");
        } else {
            System.out.println(message);
        }
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
        return new CmdArgs(input);
        // TODO think about this
    }
    public Response executeCmd(Request request) throws CmdArgsAmountException, ClassNotFoundException {
        CmdType type = request.getCmd().getCmdType();
        CmdArgs args = request.getCmdArgs();
        if (type==CmdType.COMPLEX_ARG) {
            HashMap<Field, Object> newArgs = promptComplexArgs(collectionHandler.getTargetClass());
            request.setCmdArgs(new CmdArgs(newArgs));
        } else if (type==CmdType.BOTH_ARG) {
            if (args.getArgs() == "") {
                throw new CmdArgsAmountException("This command needs an in-line argument!");
            }
            HashMap<Field, Object> complexArgs = promptComplexArgs(collectionHandler.getTargetClass());
            args.setDeconstructedObject(complexArgs);
        }
        return cmdHandler.executeCmd(request);
    }
    public HashMap<Field, Object> promptComplexArgs(Class targetClass) throws ClassNotFoundException {
        HashMap<Field, Object> map = new HashMap<>();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(UserAccessibleField.class)) {
                Object obj = promptField(field);
                map.put(field, obj);
            }
            if (field.isAnnotationPresent(UserAccessibleEnum.class)) {
                Object obj = promptEnum(field);
                map.put(field, obj);
            }
            if (field.isAnnotationPresent(UserAccessibleObject.class)) {
                Object obj = promptComplexArgs(field.getType());
                map.put(field, obj);
            }
        }
        return map;
    }
    public String promptWithValidation(Field field, String message) {
        while (true) {
            String result = promptInput(message);
            try {
                collectionHandler.validate(field, result);
                return result;
            } catch (ValueNotValidException e) {
                errorMessage(e);
                continue;
            }
        }
    }
    public String promptField(Field field) {
        return promptWithValidation(field, "Please enter " + field.getName() + ": ");

    }
    public String promptEnum(Field enumField) throws ClassNotFoundException{
        StringBuilder message = new StringBuilder("Please enter " + enumField.getName() + " (");
        Field[] enums = enumField.getType().getFields();
        for (Field field : enums) {
            message.append(field.getName()).append(";");
        }
        message.append("): ");
        return promptWithValidation(enumField, message.toString());
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
