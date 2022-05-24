package commands;


import lombok.Getter;
import message.Request;
import message.Response;


public class Requester {
    @Getter
    private final ExecutionController executionController;

    public Requester(ExecutionController executionController) {
        this.executionController = executionController;
    }

    public ActionResult request(String commandName, CommandArgs args) {
        try {
            CommandData data = executionController.getCommandData(commandName);
            Request request = executionController.createRequest(data, args);
            Response response = executionController.executeCommand(request);
            return response.getActionResult();
        } catch (Exception e) {
            e.printStackTrace();
            return new ActionResult(false, "commandNonExistentException", commandName);
        }
    }
}
