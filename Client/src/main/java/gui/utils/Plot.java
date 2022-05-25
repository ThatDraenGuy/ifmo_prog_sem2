package gui.utils;


import collection.classes.MainCollectible;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class Plot extends Pane {
    private final Axes axes;

    public Plot(Axes axes) {
        this.axes = axes;

        setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        setPrefSize(axes.getPrefWidth(), axes.getPrefHeight());
        setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

        getChildren().setAll(axes);
    }

    public void add(MainCollectible<?> collectible, Node image) {
        double x = mapX(collectible.getCoordinates().getX());
        double y = mapY(collectible.getCoordinates().getY());
        image.setLayoutX(x);
        image.setLayoutY(y);
        getChildren().add(image);
    }

    public void remove(Node node) {
        getChildren().remove(node);
    }

    private double mapX(double x) {
        double tx = axes.getPrefWidth() / 2;
        double sx = axes.getPrefWidth() /
                (axes.getXAxis().getUpperBound() -
                        axes.getXAxis().getLowerBound());

        return x * sx + tx;
    }

    private double mapY(double y) {
        double ty = axes.getPrefHeight() / 2;
        double sy = axes.getPrefHeight() /
                (axes.getYAxis().getUpperBound() -
                        axes.getYAxis().getLowerBound());

        return -y * sy + ty;
    }
}