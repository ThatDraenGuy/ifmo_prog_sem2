package collection;

import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import collection.meta.CollectibleScheme;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import locales.CollectibleFormatter;
import locales.I18N;
import lombok.Getter;
import utility.ListAndId;

import java.io.InputStream;
import java.util.*;

public class VisualViewHandler<T extends MainCollectible<?>> {
    @Getter
    private final ObjectProperty<T> selected = new ObjectPropertyBase<>() {
        @Override
        public Object getBean() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }
    };
    private Button selectedButton;
    @Getter
    private final ObservableList<Node> images = FXCollections.observableArrayList();
    private final ObservableMap<T, Node> visuals = FXCollections.observableHashMap();
    private final CollectibleScheme scheme;
    private final Map<String, Color> coloredOwners = new HashMap<>();

    private final Border defaultBorder = new Border(new BorderStroke(Color.WHITE, null, null, BorderWidths.EMPTY));
    private final Border selectedBorder = new Border(new BorderStroke(Color.AQUA, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM));

    public VisualViewHandler(Class<T> targetClass, CollectibleScheme scheme) {
        this.scheme = scheme;
        visuals.addListener(new MapChangeListener<>() {
            @Override
            public void onChanged(Change<? extends T, ? extends Node> change) {
                selected.setValue(null);
                if (change.wasAdded()) images.add(change.getValueAdded());
                if (change.wasRemoved()) images.remove(change.getValueRemoved());
            }
        });
    }

    public VisualViewHandler(Class<T> targetClass, CollectibleScheme scheme, ListAndId<T> collection) {
        this(targetClass, scheme);
        put(collection);
    }

    public void applyChange(CollectionChange<T> change) {
        Collection<T> added = change.getAddedElements();
        Collection<T> removed = change.getRemovedElements();
        Platform.runLater(() -> {
            for (T element : added) {
                visuals.put(element, sample(scheme, element));
            }
            for (T element : removed) {
                visuals.remove(element);
            }
        });
    }

    public void put(ListAndId<T> listAndId) {
        for (T element : listAndId.getList()) {
            visuals.put(element, sample(scheme, element));
        }
    }

    public void set(ListAndId<T> listAndId) {
        visuals.clear();
        put(listAndId);
    }

    private Node sample(CollectibleScheme scheme, T element) {
        InputStream imageInput = getClass().getClassLoader().getResourceAsStream("images/" + scheme.getSimpleName() + ".png");
        System.out.println("yes image");
        if (imageInput != null) {
            Button button = new Button();
            Image image = new Image(imageInput, 100, 100, true, true);
            ImageView imageView = new ImageView(image);
            button.setGraphic(imageView);
            button.setText(element.getName());
            button.setBackground(new Background(new BackgroundFill(getColoredOwner(element.getOwner()), CornerRadii.EMPTY, new Insets(1, 1, 1, 1))));
            button.setTooltip(new Tooltip("owned by: " + element.getOwner()));
            button.setOnAction((event -> {
                selected.setValue(element);
                if (selectedButton != null) selectedButton.setBorder(defaultBorder);
                selectedButton = button;
                button.setBorder(selectedBorder);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, CollectibleFormatter.getDescription(element.toModel()));
                alert.setHeaderText(element.getName());
                alert.setTitle(I18N.getGuiLabel("info"));
                alert.setGraphic(new ImageView(image));
                alert.show();
            }));
            return button;
        }
        return null;
        //TODO
    }

    private Color getColoredOwner(String owner) {
        if (coloredOwners.containsKey(owner)) return coloredOwners.get(owner);
        Color color = new Color(Math.random(), Math.random(), Math.random(), 1);
        coloredOwners.put(owner, color);
        return color;
    }
}
