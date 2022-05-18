package gui;

import app.Controllers;
import javafx.fxml.FXMLLoader;
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
        primaryStage.setMinHeight(200);
        primaryStage.setMinWidth(200);
        primaryStage.setTitle("Dracoll");
        primaryStage.setOnCloseRequest(event -> Controllers.getThreadHandler().stop());
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
