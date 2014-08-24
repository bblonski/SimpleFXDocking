package bblonski.docking;

import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Created by bblonski on 8/24/2014.
 */
public class DockingFrame {
    private final BorderPane parent;
    private final Dock dockBottom;
    private final Dock dockLeft;
    private final Dock dockRight;
    private final Dock dockTop;
    private final Dock dockCenter;

    public DockingFrame(DockController controller) {
        parent = new BorderPane();
        parent.getStylesheets().add("docking.css");
        dockBottom = controller.createDock(Side.BOTTOM);
        dockLeft = controller.createDock(Side.LEFT);
        dockRight = controller.createDock(Side.RIGHT);
        dockTop = controller.createDock(Side.TOP);
        dockCenter = controller.createDock(Side.TOP);
        dockCenter.getArea().setResizeable(false);
        HBox right = new HBox();
        HBox left = new HBox();
        VBox bottom = new VBox();
        VBox top = new VBox();
        VBox center = new VBox();
        right.getChildren().addAll(dockRight.getArea(), dockRight);
        left.getChildren().addAll(dockLeft, dockLeft.getArea());
        bottom.getChildren().addAll(dockBottom.getArea(), dockBottom);
        top.getChildren().addAll(dockTop, dockTop.getArea());
        center.getChildren().addAll(dockCenter, dockCenter.getArea());
        parent.setLeft(left);
        parent.setRight(right);
        parent.setBottom(bottom);
        parent.setTop(top);
        parent.setCenter(center);
        BorderPane.setAlignment(right, Pos.CENTER_RIGHT);
        dockRight.getArea().maxWidthProperty().bind(
                parent.widthProperty()
                        .subtract(center.minWidthProperty())
                        .subtract(left.widthProperty())
                        .subtract(dockRight.widthProperty())
                        .subtract(1)
        );
        dockLeft.getArea().maxWidthProperty().bind(
                parent.widthProperty()
                        .subtract(center.minWidthProperty())
                        .subtract(right.widthProperty())
                        .subtract(dockLeft.widthProperty())
                        .subtract(1)
        );
        dockTop.getArea().maxHeightProperty().bind(
                parent.heightProperty()
                        .subtract(bottom.heightProperty())
                        .subtract(dockTop.getHeight())
        );
        dockBottom.getArea().maxHeightProperty().bind(
                parent.heightProperty()
                        .subtract(top.heightProperty())
                        .subtract(dockBottom.getHeight())

        );
    }

    public Parent getParent() {
        return parent;
    }

    public Dock getDockBottom() {
        return dockBottom;
    }

    public Dock getDockLeft() {
        return dockLeft;
    }

    public Dock getDockRight() {
        return dockRight;
    }

    public Dock getDockTop() {
        return dockTop;
    }

    public Dock getDockCenter() {
        return dockCenter;
    }
}
