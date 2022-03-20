package console;

import annotations.NotNull;
import annotations.UserAccessibleEnum;
import annotations.UserAccessibleField;
import annotations.UserAccessibleObject;
import client.ConnectionHandler;
import collection.CollectionBuilder;
import collection.JSONToCollection;
import collection.Validator;
import collection.classes.RawDragon;
import lombok.Getter;
import message.*;

import exceptions.CommandArgsAmountException;
import exceptions.CommandExecutionException;
import exceptions.CommandNonExistentException;
import exceptions.ValueNotValidException;
import commands.*;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A class for managing interactions with user. Prompts user's input, parses it, creates a request and sends it to .
 * Gets and handles its response.
 */
public class ConsoleHandler {
    final private ConnectionHandler connectionHandler;
    final private CommandsHandler clientCommandsHandler;
    final private CollectionBuilder collectionBuilder;
    private HashMap<String, CommandData> clientCommands;
    private HashMap<String, CommandData> serverCommands;
    final private Class<?> targetClass;
    final private Scanner inputScanner;
    final private PrintStream out;
    final private PrintStream err;

    public ConsoleHandler(ConnectionHandler connectionHandler, CommandsHandler clientCommandsHandler, CollectionBuilder collectionBuilder, InputStream in, PrintStream out, PrintStream err) {
        this.connectionHandler = connectionHandler;
        this.clientCommandsHandler = clientCommandsHandler;
        this.collectionBuilder = collectionBuilder;
        this.clientCommands = clientCommandsHandler.getCommandsData();
        this.serverCommands = connectionHandler.getServerData().getServerCommands();
        this.targetClass = connectionHandler.getServerData().getTargetClass();
        inputScanner = new Scanner(in);
        this.out = out;
        this.err = err;

    }

    /**
     * Starts console's loop
     */
    public void start() {
        loop();
    }

    /**
     * A main loop. Only returns if the "Exit" command is executed or a critical exception occurs.
     */
    private void loop() {
        while (true) {
            try {
                String input = promptInput("");
                CommandRequest cmdRequest = parseInput(input);
                CommandResponse response = executeCommand(cmdRequest);
                boolean isExitQueried = handleResponse(response);
                if (isExitQueried) return;
            } catch (CommandNonExistentException | CommandArgsAmountException | CommandExecutionException e) {
                errorMessage(e);
            } catch (NoSuchElementException e) {
                errorMessage(new NoSuchElementException("InputStream ran out of lines while the program was working"));
                return;
            }
        }
    }

    /**
     * A method for prompting all of user's inputs.
     *
     * @param message Message to be displayed before reading input. If the message is an empty string, a default one will be displayed instead.
     * @return user's input
     */
    private String promptInput(String message) {
        if (message.equals("")) {
            out.println("Awaiting your input: ");
        } else {
            out.println(message);
        }
        return inputScanner.nextLine();

    }

    /**
     * A method for parsing input and creating a request out of it.
     *
     * @param input user's input
     * @return A request created from the input
     * @throws CommandNonExistentException if inputted command cannot be found in the command list {@link #parseCommandData(String)}
     * @throws CommandArgsAmountException  if user's input has more than 2 words separated by a space.
     */
    private CommandRequest parseInput(String input) throws CommandNonExistentException, CommandArgsAmountException {
        input = input.strip();
        CommandData commandData;
        CommandArgs cmdArgs = new CommandArgs("");
        if (input.contains(" ")) {
            String[] str = input.split(" ");
            input = str[0];
            cmdArgs = parseArgs(str[1]);
            if (str.length > 2) {
                throw new CommandArgsAmountException();
            }
        }
        commandData = parseCommandData(input);
        return (createRequest(commandData, cmdArgs));
    }

    private CommandRequest createRequest(CommandData commandData, CommandArgs arguments) throws CommandArgsAmountException {
        CommandType type = commandData.getCommandType();
        String argumentString = arguments.getArgs();
        boolean isEmpty = argumentString.equals("");
        switch (type) {
            case NO_ARGS:
                if (!isEmpty) {
                    throw new CommandArgsAmountException("Command \"" + commandData.getName() + "\" does not need arguments!");
                }
                break;
            case COMPLEX_ARG:
                if (!isEmpty) {
                    throw new CommandArgsAmountException("Command \"" + commandData.getName() + "\" needs a complex argument, not a simple one!");
                } else {
                    RawDragon newArgs = promptComplexArgs(targetClass);
                    arguments = new CommandArgs("", newArgs);
                }
                break;
            case SIMPLE_ARG:
                if (isEmpty) {
                    throw new CommandArgsAmountException(commandData.getName() + " needs an argument!");
                }
                break;
            case BOTH_ARG:
                if (isEmpty) {
                    throw new CommandArgsAmountException("Command \"" + commandData.getName() + "\" needs an in-line (simple) argument!");
                } else {
                    RawDragon complexArgs = promptComplexArgs(targetClass);
                    arguments = new CommandArgs(argumentString, complexArgs);
                }

        }
        return new CommandRequest(commandData, arguments);
    }

    /**
     * A method to get command from its name
     *
     * @param input a String consisting of a command name
     * @return The command with corresponding name
     * @throws CommandNonExistentException if inputted command cannot be found in the command list
     */
    private CommandData parseCommandData(String input) throws CommandNonExistentException {
        if (isInCommands(input)) {
            if (isClientCommand(input)) {
                return clientCommands.get(input);
            } else {
                return serverCommands.get(input);
            }
        } else {
            throw new CommandNonExistentException(input);
        }
    }

    private boolean isInCommands(String name) {
        return (serverCommands.containsKey(name) || clientCommands.containsKey(name));
        //TODO wtf?
    }

    private boolean isClientCommand(String name) {
        return clientCommands.containsKey(name);
    }

    private boolean isClientCommand(CommandRequest request) {
        return isClientCommand(request.getCommandData().getName());
        //TODO rework
    }

    /**
     * A method that gets a String and returns a CmdArgs object created with it
     */
    private CommandArgs parseArgs(String input) {
        return new CommandArgs(input);
    }

    /**
     * A method that gets a request and invokes ... method.
     * Also checks if command needs a complex argument and invokes {@link #promptComplexArgs(Class)} if it does.
     *
     * @param request a request for a command execution
     * @return response gotten from command's execution
     * @throws CommandArgsAmountException if command requires both a simple and a complex argument and a simple one wasn't provided
     */
    private CommandResponse executeCommand(CommandRequest request) throws CommandArgsAmountException {
        if (isClientCommand(request)) {
            return clientCommandsHandler.executeCommand(request);
        }
        return connectionHandler.send(request);
    }

    private RawDragon promptComplexArgs(Class<?> targetClass) {
        ArrayList<String> fieldNames = collectionBuilder.getFieldNames();
        for (String field : fieldNames) {
            promptField(field);
        }
        return collectionBuilder.build();
//        Method[] methods = RawDragon.builder().getClass().getDeclaredMethods();
//        for (Method method : methods) {
//            if (!method.getName().equals("build")) {
//                method.invoke(null, promptField(method));
//            }
//        }
//        Field[] fields = targetClass.getDeclaredFields();
//        for (Field field : fields) {
//            if (field.isAnnotationPresent(UserAccessibleField.class)) {
//                Object obj = promptField(field);
//                map.put(field, obj);
//            }
//            if (field.isAnnotationPresent(UserAccessibleEnum.class)) {
//                Object obj = promptEnum(field);
//                map.put(field, obj);
//            }
//            if (field.isAnnotationPresent(UserAccessibleObject.class)) {
//                if (!field.isAnnotationPresent(NotNull.class)) {
//                    boolean answer = promptAgreement("Object \"" + field.getName() + "\" can be null. Are you gonna input it?");
//                    if (answer) {
//                        Object obj = promptComplexArgs(field.getType());
//                        map.put(field, obj);
//                    }
//                } else {
//                    Object obj = promptComplexArgs(field.getType());
//                    map.put(field, obj);
//                }
//
//            }
//        }
//        return map;
    }

    private void promptField(String field) {
        while (true) {
            String result = promptInput("Please enter " + field + ": ");
            try {
                collectionBuilder.put(field, result);
                return;
            } catch (ValueNotValidException e) {
                errorMessage(e);
            }
        }
    }
//    private <T> T promptWithValidation(Method field, String message) {
//        while (true) {
//            String result = promptInput(message);
//            try {
//                return (T) Validator.validate(field, result);
//                //TODO change validator!!!!
//            } catch (ValueNotValidException e) {
//                errorMessage(e);
//            }
//        }
//    }
//
//
//    /**
//     * A method to prompt an enum-type field. Simply invokes {@link #promptWithValidation(Field, String)} with a custom message
//     */
//    private Object promptEnum(Field enumField) {
//        StringBuilder message = new StringBuilder("Please enter " + enumField.getName() + " (");
//        Field[] enums = enumField.getType().getFields();
//        for (Field field : enums) {
//            message.append(field.getName()).append(";");
//        }
//        message.deleteCharAt(message.length() - 1);
//        message.append("): ");
//        return promptWithValidation(enumField, message.toString());
//    }

    /**
     * A method to ask user for agreement. Returns a boolean result: true if the answer is "yes" and false if the answer is "no".
     */
    public boolean promptAgreement(String message) {
        while (true) {
            String answer = promptInput(message + " (Y/N)");
            switch (answer) {
                case "Y":
                    return true;
                case "N":
                    return false;
                default:
                    out.println("Please input \"Y\" or \"N\"");
            }
        }
    }

    /**
     * A method to handle command's execution's response. Gets ActionResult from it and displays it.
     *
     * @param response a response gotten from ...
     * @return a boolean value: if the response is from an "Exit" command it's true, otherwise it's false.
     * @throws CommandExecutionException if ActionResult isn't success
     */
    private boolean handleResponse(CommandResponse response) throws CommandExecutionException {
        ActionResult result = response.getActionResult();
        boolean isSuccess = result.isSuccess();
        if (isSuccess) {
            String message = result.getMessage();
            out.println(message);
            return result.isConsoleExitQueried();
        } else {
            throw new CommandExecutionException(result);
        }
    }

    /**
     * A simple method that displays exception's message
     */
    private void errorMessage(Exception e) {
        err.println(e.getMessage());
    }
}
