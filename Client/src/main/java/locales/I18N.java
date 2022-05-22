package locales;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.text.MessageFormat;
import java.util.*;

public class I18N {
    private static ResourceBundle guiLabels;
    private static ResourceBundle collectibles;
    private static ResourceBundle interactions;
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

    public static String getCollectible(String key, Object... args) {
        if (!collectibles.containsKey(key)) return key;
        MessageFormat format = new MessageFormat(collectibles.getString(key));
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) args[i] = "";
        }
        return format.format(args);
    }

    public static String getInteraction(String key, Object... args) {
        if (!interactions.containsKey(key)) return key;
        MessageFormat format = new MessageFormat(interactions.getString(key));
        return format.format(args);
    }

    public static StringBinding getGuiLabelBinding(String key) {
        return Bindings.createStringBinding(() -> getGuiLabel(key), locale);
    }

    public static StringBinding getCollectibleBinding(String key, Object... args) {
        return Bindings.createStringBinding(() -> getCollectible(key, args), locale);
    }

    public static StringBinding getCollectibleBinding(String key, String appendix) {
        return Bindings.createStringBinding(() -> getCollectible(key) + appendix, locale);
    }

    public static void setLocale(Locale locale) {
        if (locale != null) {
            guiLabels = ResourceBundle.getBundle("locales.GuiLabels", locale);
            collectibles = ResourceBundle.getBundle("locales.Collectibles", locale);
            interactions = ResourceBundle.getBundle("locales.Interactions", locale);
            I18N.locale.setValue(locale);
        }
    }
}
