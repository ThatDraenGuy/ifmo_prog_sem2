package app;

import collection.CollectionClassesHandler;
import commands.Requester;
import gui.SceneController;
import lombok.Getter;
import lombok.Setter;
import threads.ThreadHandler;

public class Controllers {
    //    @Getter
//    @Setter
//    private static ConsoleInteractionController interactionController;
//    @Getter
//    @Setter
//    private static ExecutionController executionController;
    @Getter
    @Setter
    private static Requester requester;
    @Getter
    @Setter
    private static SceneController sceneController;
    @Getter
    @Setter
    private static ThreadHandler threadHandler;
    @Getter
    @Setter
    private static CollectionClassesHandler collectionClassesHandler;
}
