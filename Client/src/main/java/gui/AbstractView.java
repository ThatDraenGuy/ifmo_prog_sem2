package gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;
import locales.I18N;

import java.util.Locale;

public abstract class AbstractView {
    protected ChoiceBox<Locale> localeChoiceBox;
    protected void defaultInitialize(BooleanProperty progressVisibility, StringProperty message, ViewModel viewModel) {
        progressVisibility.bind(viewModel.isTaskRunning());
        message.bind(viewModel.getMessage());
        viewModel.getErrorMessage().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue != null && !newValue.isEmpty()) {
                        Alert alert = new Alert(
                                Alert.AlertType.ERROR, newValue
                        );
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
                return object.getDisplayName();
            }

            @Override
            public Locale fromString(String string) {
                for (Locale locale : I18N.getSupportedLocales()) {
                    if (locale.getDisplayName().equals(string)) return locale;
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
