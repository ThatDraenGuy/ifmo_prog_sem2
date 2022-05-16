package gui;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.function.Consumer;

public class Utilities {


    public static Service<Void> getDefaultService(Consumer<Void> action) {
        return new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        updateProgress(0.1, 1.0);
                        action.accept(null);
                        return null;
                    }
                };
            }
        };
    }
}
