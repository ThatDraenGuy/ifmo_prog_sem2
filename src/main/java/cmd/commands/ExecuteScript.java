package cmd.commands;

import Console.ConsoleHandler;
import cmd.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class ExecuteScript extends AbstractCommand {
    private CmdHandler cmdHandler;
    private HashSet<String> scriptHistory;
    public ExecuteScript(CmdHandler cmdHandler) {
        super("execute_script", "считать и исполнить скрипт из указанного файла", CmdType.SIMPLE_ARG);
        this.cmdHandler=cmdHandler;
        this.scriptHistory=new HashSet<>();
    }

    @Override
    public ActionResult action(CmdArgs args) {
        try {
            File scriptFile = new File(args.getArgs());
            Scanner fileScanner = new Scanner(scriptFile);
            if (!scriptHistory.add(scriptFile.getAbsolutePath())) {
                return new ActionResult(false, "Script recursion detected!");
            }
            StringBuilder script = new StringBuilder();
            while (fileScanner.hasNextLine()) {
                script.append(fileScanner.nextLine()).append("\n");
            }
            script.append("exit");
            InputStream in = new ByteArrayInputStream(script.toString().getBytes());
            OutputStream out = new ByteArrayOutputStream();
            PrintStream outPrint = new PrintStream(out);
            OutputStream err = new ByteArrayOutputStream();
            PrintStream errPrint = new PrintStream(err);
            ConsoleHandler consoleHandler = new ConsoleHandler(cmdHandler, in, outPrint, errPrint);
            try {
                consoleHandler.start();
            } catch (NoSuchElementException e) {
                return new ActionResult(false, "Reached end of the script in the process of executing command!");
            }
            String outRes = out.toString();
            String errRes = err.toString();
            if (errRes.equals("")) {
                return new ActionResult(true, "Successfully executed script.");
            } else {
                return new ActionResult(false, "An exception(s) occurred while trying to execute the script. Here's a full exception log: \n"+errRes);
            }
        }  catch (FileNotFoundException e) {
            return new ActionResult(false, "Cannot find file \""+args.getArgs()+"\"");
        }
    }
}
