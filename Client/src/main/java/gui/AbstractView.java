package gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;
import locales.I18N;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public abstract class AbstractView {
    protected ChoiceBox<Locale> localeChoiceBox;
    protected final Timer timer = new Timer();
    protected void defaultInitialize(BooleanProperty progressVisibility, StringProperty message, ViewModel viewModel) {
        progressVisibility.bind(viewModel.isTaskRunning());
        message.bindBidirectional(viewModel.getMessage());
        message.addListener((observable -> timer.schedule(new TimerTask() {
            @Override
            public void run() {
                message.setValue("");
            }
        }, 5000)));
        viewModel.getErrorMessage().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue != null && !newValue.isEmpty()) {
                        Alert alert = new Alert(
                                Alert.AlertType.ERROR, newValue
                        );
                        alert.setTitle(I18N.getGuiLabel("error"));
                        alert.setHeaderText(I18N.getGuiLabel("error"));
                        alert.showAndWait();
                    }
                }
        );
        bindLocales();
        generateLocaleChoice();
    }

    private void generateLocaleChoice() {
        localeChoiceBox = new ChoiceBox<>(I18N.getSupportedLocales());
        localeChoiceBox.setValue(I18N.getLocale().getValue());
        localeChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Locale object) {
                if (object == null) return "";
                return object.getDisplayName(object);
            }

            @Override
            public Locale fromString(String string) {
                for (Locale locale : I18N.getSupportedLocales()) {
                    if (locale.getDisplayName(locale).equals(string)) return locale;
                }
                return I18N.getLocale().getValue();
            }
        });
        localeChoiceBox.setOnAction((event -> I18N.setLocale(localeChoiceBox.getValue())));
        I18N.getLocale().addListener(((observable, oldValue, newValue) -> localeChoiceBox.setValue(newValue)));
        placeLocaleChoiceBox();
    }

    protected abstract void placeLocaleChoiceBox();

    public abstract void initialize();

    public abstract void bindLocales();
}
