package gui;

import app.Controllers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import locales.I18N;

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
        primaryStage.titleProperty().bind(I18N.getGuiLabelBinding("appName"));
        primaryStage.setOnCloseRequest(event -> Controllers.getThreadHandler().stop());
    }

    public void switchToLoginScene() {
        switchTo(loginScene);
    }

    public void switchToMainScene() {
        switchTo(mainScene);
    }

    public void switchToConnectScene() {
        switchTo(connectScene);
    }

    private void switchTo(Scene scene) {
        primaryStage.setScene(scene);
        Notifications.publish(Notifications.SCENE_CHANGE_EVENT);
        primaryStage.show();
        primaryStage.setMinHeight(scene.getHeight());
        primaryStage.setMinWidth(scene.getWidth());
    }
}
