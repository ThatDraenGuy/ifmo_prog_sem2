package app;

import commands.ExecutionController;
import commands.Requester;
import gui.SceneController;
import lombok.Getter;
import lombok.Setter;
import threads.ConsoleInteractionController;

public class Controllers {
    @Getter
    @Setter
    private static ConsoleInteractionController interactionController;
    @Getter
    @Setter
    private static ExecutionController executionController;
    @Getter
    @Setter
    private static Requester requester;
    @Getter
    @Setter
    private static SceneController sceneController;
}
