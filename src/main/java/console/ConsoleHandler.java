package console;

import annotations.NotNull;
import collection.classes.MainCollectible;
import exceptions.CmdArgsAmountException;
import exceptions.CommandExecutionException;
import exceptions.CommandNonExistentException;
import exceptions.ValueNotValidException;
import annotations.UserAccessibleEnum;
import annotations.UserAccessibleField;
import annotations.UserAccessibleObject;
import cmd.*;
import common.CmdRequest;
import common.Request;
import common.Response;

import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A class for managing interactions with user. Prompts user's input, parses it, creates a request and sends it to {@link cmd.CmdHandler}.
 * Gets and handles its response.
 */
public class ConsoleHandler {
    final private CmdHandler cmdHandler;
    final private Class<? extends MainCollectible> targetClass;
    final private Scanner inputScanner;
    final private PrintStream out;
    final private PrintStream err;

    public ConsoleHandler(CmdHandler cmdHandler, InputStream in, PrintStream out, PrintStream err) {
        this.cmdHandler = cmdHandler;
        this.targetClass = cmdHandler.getCollectionHandler().getTargetClass();
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
                Request cmdRequest = parseInput(input);
                Response response = executeCmd(cmdRequest);
                boolean isCmdExit = handleResponse(response);
                if (isCmdExit) {
                    if (promptAgreement("Are you sure you want to exit? Any unsaved changes will be gone!")) return;
                }
            } catch (CommandNonExistentException | CmdArgsAmountException | CommandExecutionException e) {
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
     * @throws CommandNonExistentException if inputted command cannot be found in the command list {@link #parseCommand(String)}
     * @throws CmdArgsAmountException      if user's input has more than 2 words separated by a space.
     */
    private Request parseInput(String input) throws CommandNonExistentException, CmdArgsAmountException {
        input = input.strip();
        Command cmd;
        CmdArgs cmdArgs = new CmdArgs("");
        if (input.contains(" ")) {
            String[] str = input.split(" ");
            input = str[0];
            cmdArgs = parseArgs(str[1]);
            if (str.length > 2) {
                throw new CmdArgsAmountException();
            }
        }
        cmd = parseCommand(input);
        return (new CmdRequest(cmd, cmdArgs));
    }

    /**
     * A method to get command from its name
     *
     * @param input a String consisting of a command name
     * @return The command with corresponding name
     * @throws CommandNonExistentException if inputted command cannot be found in the command list
     */
    private Command parseCommand(String input) throws CommandNonExistentException {
        if (cmdHandler.isInCmds(input)) {
            return cmdHandler.getCmds().get(input);
        } else {
            throw new CommandNonExistentException(input);
        }
    }

    /**
     * A method that gets a String and returns a CmdArgs object created with it
     */
    private CmdArgs parseArgs(String input) {
        return new CmdArgs(input);
    }

    /**
     * A method that gets a request and invokes {@link cmd.CmdHandler}'s {@link cmd.CmdHandler#executeCmd(Request)} method.
     * Also checks if command needs a complex argument and invokes {@link #promptComplexArgs(Class)} if it does.
     *
     * @param request a request for a command execution
     * @return response gotten from command's execution
     * @throws CmdArgsAmountException if command requires both a simple and a complex argument and a simple one wasn't provided
     */
    private Response executeCmd(Request request) throws CmdArgsAmountException {
        CmdType type = request.getCmd().getCmdType();
        CmdArgs args = request.getCmdArgs();
        if (type == CmdType.COMPLEX_ARG) {
            HashMap<Field, Object> newArgs = promptComplexArgs(targetClass);
            request.setCmdArgs(new CmdArgs(newArgs));
        } else if (type == CmdType.BOTH_ARG) {
            if (args.getArgs().equals("")) {
                throw new CmdArgsAmountException("This command needs an in-line argument!");
            }
            HashMap<Field, Object> complexArgs = promptComplexArgs(targetClass);
            args.setDeconstructedObject(complexArgs);
        }
        return cmdHandler.executeCmd(request);
    }

    /**
     * A method to prompt a complex argument from user. A complex argument is a deconstructed version of a Collectible object,
     * created by asking user to input each of its user accessible fields. Invokes {@link #promptField(Field)} and
     * {@link #promptEnum(Field)}.
     * If any of class' fields is also a Collectible object, this method is invoked recursively for that field
     *
     * @param targetClass a Class object of a desired Collectible object
     * @return a deconstructed version of a Collectible object
     */
    private HashMap<Field, Object> promptComplexArgs(Class<?> targetClass) {
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
                if (!field.isAnnotationPresent(NotNull.class)) {
                    boolean answer = promptAgreement("Object \""+ field.getName()+"\" can be null. Are you gonna input it?");
                    if (answer) {
                        Object obj = promptComplexArgs(field.getType());
                        map.put(field, obj);
                    }
                } else {
                    Object obj = promptComplexArgs(field.getType());
                    map.put(field, obj);
                }

            }
        }
        return map;
    }

    /**
     * A method that invokes {@link #promptInput(String)} and validates gotten user's input. Also converts it to a needed type.
     * To do that it invokes {@link collection.CollectionHandler#validate(Field, String)} and catches a {@link exceptions.ValueNotValidException}
     * produced by it. The exception is than passed to the {@link #errorMessage(Exception)}, after what method asks user's input again.
     *
     * @param field   a field corresponding to the asked input. Validation requirements and type the input needs to be  cast to a gotten from it.
     * @param message a message to be passed down to the {@link #promptInput(String)}
     * @return validated and converted version of user's input
     */
    private Object promptWithValidation(Field field, String message) {
        while (true) {
            String result = promptInput(message);
            try {
                return cmdHandler.getCollectionHandler().validate(field, result);
            } catch (ValueNotValidException e) {
                errorMessage(e);
            }
        }
    }

    /** A method to prompt a field. Simply invokes {@link #promptWithValidation(Field, String)} with a custom message*/
    private Object promptField(Field field) {
        return promptWithValidation(field, "Please enter " + field.getName() + ": ");

    }

    /** A method to prompt an enum-type field. Simply invokes {@link #promptWithValidation(Field, String)} with a custom message*/
    private Object promptEnum(Field enumField) {
        StringBuilder message = new StringBuilder("Please enter " + enumField.getName() + " (");
        Field[] enums = enumField.getType().getFields();
        for (Field field : enums) {
            message.append(field.getName()).append(";");
        }
        message.deleteCharAt(message.length()-1);
        message.append("): ");
        return promptWithValidation(enumField, message.toString());
    }

    /** A method to ask user for agreement. Returns a boolean result: true if the answer is "yes" and false if the answer is "no".*/
    private boolean promptAgreement(String message) {
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
     * @param response a response gotten from {@link cmd.CmdHandler}
     * @return a boolean value: if the response is from an "Exit" command it's true, otherwise it's false.
     * @throws CommandExecutionException if ActionResult isn't success
     */
    private boolean handleResponse(Response response) throws CommandExecutionException {
        ActionResult result = response.getActionResult();
        boolean isSuccess = result.isSuccess();
        if (isSuccess) {
            String message = result.getMessage();
            if (message.equals("exit")) {
                return true;
            } else {
                out.println(result.getMessage());
                return false;
            }
        } else {
            throw new CommandExecutionException(result);
        }
    }

    /** A simple method that displays exception's message */
    private void errorMessage(Exception e) {
        err.println(e.getMessage());
    }
}
