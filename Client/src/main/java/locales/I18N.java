package locales;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.*;

public class I18N {
    private static ResourceBundle guiLabels;
    private static ResourceBundle collectibles;
    @Getter
    private static final ObjectProperty<Locale> locale;
    @Getter
    private static final ObservableList<Locale> supportedLocales = FXCollections.observableArrayList();

    static {
        locale = new SimpleObjectProperty<>(Locale.forLanguageTag("ru-RU"));
//        locale.addListener(((observable, oldValue, newValue) -> {
//            if (newValue==null) locale.setValue(oldValue);
//            else {
//                try {
//                    setLocale(newValue);
//                } catch (MissingResourceException e) {
//                    locale.setValue(oldValue);
//                }
//            }
//        }));
        supportedLocales.add(Locale.forLanguageTag("ru-RU"));
        supportedLocales.add(Locale.forLanguageTag("en-IE"));
        setLocale(locale.getValue());
    }

    public static String getGuiLabel(String key) {
        if (guiLabels.containsKey(key)) return guiLabels.getString(key);
        return key;
    }

    public static String getCollectible(String key) {
        if (collectibles.containsKey(key)) return collectibles.getString(key);
        return key;
    }

    public static StringBinding getGuiLabelBinding(String key) {
        return Bindings.createStringBinding(() -> getGuiLabel(key), locale);
    }

    public static StringBinding getCollectibleBinding(String key) {
        return Bindings.createStringBinding(() -> getCollectible(key), locale);
    }

    public static StringBinding getCollectibleBinding(String key, String appendix) {
        return Bindings.createStringBinding(() -> getCollectible(key) + appendix, locale);
    }

    public static void setLocale(Locale locale) {
        if (locale != null) {
            guiLabels = ResourceBundle.getBundle("locales.GuiLabels", locale);
            collectibles = ResourceBundle.getBundle("locales.Collectibles", locale);
            I18N.locale.setValue(locale);
        }
    }
}
