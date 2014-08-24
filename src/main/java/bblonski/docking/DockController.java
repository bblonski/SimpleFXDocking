package bblonski.docking;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by bblonski on 8/22/2014.
 */
public class DockController {
    private final Stage markerStage;
    private final Set<Dock> docks = new HashSet<>();
    private final Map<Dockable, Pane> content = new HashMap<>();

    public DockController() {
        markerStage = new Stage();
        markerStage.initStyle(StageStyle.UNDECORATED);
        Rectangle dummy = new Rectangle(3, 10, Color.web("#555555"));
        StackPane markerStack = new StackPane();
        markerStack.getChildren().add(dummy);
        markerStage.setScene(new Scene(markerStack));
    }

    public Dock createDock(Side side) {
        return new Dock(this, side);
    }

    public Dockable createDockable(String text, Dock dock) {
        final Dockable dockable = new Dockable(text, dock);
        if(dock.getSelected() == null) {
            dock.setSelected(dockable);
        }
//        dockable.getContent().setStyle("-fx-border-width: 1; -fx-border-color: darkblue");
        content.put(dockable, dockable.getContent());
        return dockable;
    }

    Stage getDockStage() {
        return markerStage;
    }

    Set<Dock> getAllDocks() {
        return docks;
    }

    private Rectangle2D getAbsoluteRect(Control node) {
        return new Rectangle2D(node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getX() + node.getScene().getWindow().getX(),
                node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getY() + node.getScene().getWindow().getY(),
                node.getWidth(),
                node.getHeight());
    }

    void removeDock(Dock dock) {
        docks.remove(dock);
    }

    void registerDock(Dock dock) {
        docks.add(dock);
    }

    public DockingFrame getBorderPane() {
        return new DockingFrame(this);
    }
}
