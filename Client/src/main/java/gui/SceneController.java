package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    private final Stage primaryStage;
    private final Scene connectScene;
    private final Scene loginScene;
    private final Scene mainScene;


    public SceneController(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        connectScene = new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("gui/connectScene.fxml")));
        loginScene = new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("gui/loginScene.fxml")));
        mainScene = new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("gui/mainScene.fxml")));

    }

    public void switchToLoginScene() {
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public void switchToMainScene() {
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public void switchToConnectScene() {
        primaryStage.setScene(connectScene);
        primaryStage.show();
    }
}
