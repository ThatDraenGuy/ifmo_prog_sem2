package commands.instances;

import web.ConnectionHandler;
import threads.ThreadHandler;
import commands.*;

import java.io.IOException;
import java.net.UnknownHostException;

public class Connect extends AbstractCommand {
    private final ConnectionHandler connectionHandler;
    private final ThreadHandler threadHandler;
    private final ExecutionController executionController;

    public Connect(ConnectionHandler connectionHandler, ExecutionController executionController, ThreadHandler threadHandler) {
        super("connect", "подключиться к серверу", new CommandArgsInfo(CommandArgsType.SIMPLE_ARG), CommandAccessLevel.DISCONNECTED);
        this.connectionHandler = connectionHandler;
        this.executionController = executionController;
        this.threadHandler = threadHandler;
    }

    @Override
    public ActionResult action(ExecutionPayload executionPayload) {
        CommandArgs args = executionPayload.getCommandArgs();
        try {
            String address = args.getArgs();
            String[] splitStr = address.split(":");
            if (splitStr.length != 2)
                return new ActionResult(false, "Invalid input");
            String host = splitStr[0];
            int port = Integer.parseInt(splitStr[1]);
            connectionHandler.connect(host, port);
            threadHandler.reloadMessageReader();
            executionController.setUserAccessLevel(CommandAccessLevel.GUEST);
            return new ActionResult(true, "You have been successfully connected");
        } catch (NumberFormatException e) {
            return new ActionResult(false, "Couldn't parse port");
        } catch (UnknownHostException e) {
            return new ActionResult(false, "Couldn't find host \"" + args.getArgs() + "\"");
        } catch (IOException e) {
            return new ActionResult(false, e.getMessage());
        }
    }
}
