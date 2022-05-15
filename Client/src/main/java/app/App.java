package app;

import gui.SceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public void main() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneController sceneController = new SceneController(primaryStage);
        Controllers.setSceneController(sceneController);
        sceneController.switchToConnectScene();
//        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/connectScene.fxml"));
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();
    }
}
