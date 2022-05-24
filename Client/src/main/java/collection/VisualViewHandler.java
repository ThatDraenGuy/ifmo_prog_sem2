package collection;

import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import collection.meta.CollectibleScheme;
import gui.Notifications;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
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
import javafx.util.Duration;
import locales.CollectibleFormatter;
import locales.I18N;
import lombok.Getter;
import utility.ListAndId;

import java.io.InputStream;
import java.util.*;

public class VisualViewHandler<T extends MainCollectible<?>> {
    private final ClientCollectionHandler<T> collectionHandler;
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
    private final ObservableMap<T, Transition> animations = FXCollections.observableHashMap();
    private final CollectibleScheme scheme;
    private final Map<collection.classes.Color, Image> variants = new HashMap<>();
    private final Map<String, Color> coloredOwners = new HashMap<>();

    private final Border defaultBorder = new Border(new BorderStroke(Color.WHITE, null, null, BorderWidths.EMPTY));
    private final Border selectedBorder = new Border(new BorderStroke(Color.AQUA, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM));

    public VisualViewHandler(ClientCollectionHandler<T> clientCollectionHandler) {
        this.scheme = clientCollectionHandler.getCollectibleScheme();
        visuals.addListener(new MapChangeListener<>() {
            @Override
            public void onChanged(Change<? extends T, ? extends Node> change) {
                selected.setValue(null);
                if (change.wasAdded()) images.add(change.getValueAdded());
                if (change.wasRemoved()) images.remove(change.getValueRemoved());
            }
        });
        Notifications.subscribe(CollectionClassesHandler.COLLECTION_SET_EVENT, this, this::set);
        Notifications.subscribe(CollectionClassesHandler.COLLECTION_CHANGE_EVENT, this, this::applyChange);
        put(clientCollectionHandler.getCollection());
        this.collectionHandler = clientCollectionHandler;
    }

    public void applyChange(String event) {
        CollectionChange<T> change = collectionHandler.getLastChange();
        Collection<T> added = change.getAddedElements();
        Collection<T> removed = change.getRemovedElements();
        Platform.runLater(() -> {
            for (T element : added) {
                visuals.put(element, sample(element));
            }
            for (T element : removed) {
                visuals.remove(element);
            }
        });
    }

    public void put(ListAndId<T> listAndId) {
        for (T element : listAndId.getList()) {
            Node node = sample(element);
            visuals.put(element, node);
        }
    }

    public void set(String event) {
        ListAndId<T> listAndId = collectionHandler.getCollection();
        visuals.clear();
        put(listAndId);
    }

    private Transition getAnimation(Node node) {
        FadeTransition transition = new FadeTransition(new Duration(3000), node);
        transition.setFromValue(0.0);
        transition.setToValue(1.0);
        return transition;
    }

    private Image getImage(T element) {
        collection.classes.Color color = element.getColor();
        if (variants.containsKey(color)) return variants.get(color);
        InputStream imageInput = getClass().getClassLoader().getResourceAsStream("images/" + scheme.getSimpleName() + "/" + element.getColor() + ".png");
        Image image = new Image(imageInput, 100, 100, true, true);
        variants.put(color, image);
        return image;
    }

    private Node sample(T element) {
        Image image = getImage(element);
        Button button = new Button();
        ImageView imageView = new ImageView(image);
        button.setGraphic(imageView);
        button.setText(element.getName());
        button.setBackground(new Background(new BackgroundFill(getColoredOwner(element.getOwner()), CornerRadii.EMPTY, new Insets(1, 1, 1, 1))));
        Tooltip tooltip = new Tooltip();
        tooltip.textProperty().bind(I18N.getGuiLabelBinding("ownedBy", element.getOwner()));
        button.setTooltip(tooltip);
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
        animations.put(element, getAnimation(button));
        return button;
    }

    private Color getColoredOwner(String owner) {
        if (coloredOwners.containsKey(owner)) return coloredOwners.get(owner);
        Color color = new Color(Math.random(), Math.random(), Math.random(), 1);
        coloredOwners.put(owner, color);
        return color;
    }

    public void playAnimation() {
        for (Transition transition : animations.values()) {
            transition.playFromStart();
        }
    }
}
