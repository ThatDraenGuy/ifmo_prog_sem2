package Console;

import Collection.Classes.Dragon;
import Collection.CollectionHandler;
import Exceptions.CmdArgsAmountException;
import Exceptions.CommandExecutionException;
import Exceptions.CommandNonExistentException;
import annotations.UserAccessibleEnum;
import annotations.UserAccessibleField;
import annotations.UserAccessibleObject;
import cmd.*;
import common.CmdRequest;
import common.Request;
import common.Response;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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
                String input = promptInput("");
                Request cmdRequest = parseInput(input);
                Response response = executeCmd(cmdRequest);
                handleResponse(response);
            } catch (CommandNonExistentException | CmdArgsAmountException | CommandExecutionException e) {
                errorMessage(e);
                continue;
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
        CmdArgs args = new CmdArgs(input);
        return args;
        // TODO think about this
    }
    public Response executeCmd(Request request) throws CmdArgsAmountException, ClassNotFoundException {
        CmdType type = request.getCmd().getCmdType();
        if (type==CmdType.COMPLEX_ARG) {
            request.setCmdArgs(new CmdArgs(promptComplexArgs(Dragon.class)));
        } //TODO get more abstract from dragons
        return cmdHandler.executeCmd(request);
    }
    public String promptComplexArgs(Class targetClass) throws ClassNotFoundException {
        //TODO change to Hashmap
        Field[] fields = targetClass.getFields();
        StringBuilder args= new StringBuilder();
        for (Field field : fields) {
            String res = null;
            Annotation annField = field.getAnnotation(UserAccessibleField.class);
            if (annField!=null) {
                res=promptInput("Please enter " + field.getName() + ": ");
            }
            Annotation annEnum = field.getAnnotation(UserAccessibleEnum.class);
            if (annEnum!=null) {
                res=promptEnum(field.getType().getName());
            }
            Annotation annObject = field.getAnnotation(UserAccessibleObject.class);
            if (annObject!=null) {
                res=promptComplexArgs(field.getType());
            }
            args.append(field.getName()).append(":").append(res).append(";");
        }
        return args.toString();
        //TODO implement
    }
    public String promptEnum(String enumName) throws ClassNotFoundException{
        StringBuilder message = new StringBuilder("Please enter " + enumName + " (");
        Field[] enums = Class.forName(enumName).getFields();
        for (Field field: enums) {
            message.append(field.getName()).append(";");
        }
        message.append("): ");
        return promptInput(message.toString());
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
