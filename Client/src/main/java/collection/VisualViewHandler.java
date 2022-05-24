package collection;

import collection.classes.MainCollectible;
import collection.meta.CollectibleScheme;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.*;
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

import java.io.InputStream;
import java.util.*;

public class VisualViewHandler<T extends MainCollectible<?>> {
    private final ObservableCollection<T> observableCollection;
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

    public VisualViewHandler(ObservableCollection<T> observableCollection) {
        this.observableCollection = observableCollection;
        this.scheme = observableCollection.getCollectibleScheme();
        observableCollection.getItems().addListener((ListChangeListener<T>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    for (T removed : c.getRemoved()) {
                        visuals.remove(removed);
                    }
                }
                if (c.wasAdded()) {
                    for (T added : c.getAddedSubList()) {
                        visuals.put(added, sample(added));
                    }
                }
            }
        });
        visuals.addListener((MapChangeListener<T, Node>) change -> Platform.runLater(() -> {
            selected.setValue(null);
            if (change.wasAdded()) images.add(change.getValueAdded());
            if (change.wasRemoved()) images.remove(change.getValueRemoved());
        }));
        for (T element : observableCollection.getItems()) {
            visuals.put(element, sample(element));
        }
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
