package console;


import exceptions.ValueNotValidException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

/**
 * A class for managing interactions with user. Prompts user's input, parses it, creates a request and sends it to .
 * Gets and handles its response.
 */
public class ConsoleHandler {

    final private Scanner inputScanner;
    final private PrintStream out;
    final private PrintStream err;

    final private AgreementHandler agreementHandler;

    public ConsoleHandler(InputStream in, PrintStream out, PrintStream err) {
        inputScanner = new Scanner(in);
        this.out = out;
        this.err = err;
        this.agreementHandler = new AgreementHandler();
    }


    /**
     * A method for prompting all of user's inputs.
     *
     * @param message Message to be displayed before reading input. If the message is an empty string, a default one will be displayed instead.
     * @return user's input
     */
    public String promptInput(String message) {
        if (message.equals("")) {
            out.println("Awaiting your input: ");
        } else {
            out.println(message);
        }
        return inputScanner.nextLine();

    }

    /**
     * A method to ask user for agreement. Returns a boolean result: true if the answer is "Y" and false if the answer is "N".
     */
    public boolean promptAgreement(String message) {
        while (true) {
            String answer = promptInput(message + " (Y/N)");
            try {
                return agreementHandler.getAgreement(answer);
            } catch (ValueNotValidException e) {
                message("Please input \"Y\" or \"N\" (or some other sensible representation of your agreement/disagreement)");
            }
        }
    }

    public void message(String str) {
        out.println(str);
    }

    public void debugMessage(String str) {
        message(str);
    }

    /**
     * A simple method that displays exception's message
     */
    public void errorMessage(Exception e) {
        err.println(e.getMessage());
    }
}
