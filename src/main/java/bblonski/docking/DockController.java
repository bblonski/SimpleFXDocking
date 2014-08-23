package bblonski.docking;

import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
    private final Set<TabPane> tabPanes = new HashSet<>();
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

    public Dock createDock() {
        return new Dock(this, Side.TOP);
    }

    public Dock createDock(Side side) {
        return new Dock(this, side);
    }

    static int i = 1;

    public Dockable createDockable(String text, Dock dock) {
        final Dockable dockable = new Dockable(text, dock, this);
        if(dock.getSelected() == null) {
            dock.setSelected(dockable);
        }
//        dockable.getContent().setStyle("-fx-border-width: 1; -fx-border-color: darkblue");
        dockable.getContent().getChildren().add(new Label("pane " + i++));
        content.put(dockable, dockable.getContent());
        return dockable;
    }

    public TabPane createTabPane(Side side) {
        final TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setSide(side);
        tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);
        final DraggableTab tab = new DraggableTab("Test", this);
        tabPane.getTabs().addAll(tab);
        registerTabPane(tabPane);
        tabPane.setRotateGraphic(true);
        return tabPane;
    }

    Stage getDockStage() {
        return markerStage;
    }

    Set<Dock> getAllDocks() {
        return docks;
    }

    void registerTabPane(TabPane tabPane) {
       getTabPanes().add(tabPane);
    }

    Set<TabPane> getTabPanes() {
        return tabPanes;
    }

    private Rectangle2D getAbsoluteRect(Control node) {
        return new Rectangle2D(node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getX() + node.getScene().getWindow().getX(),
                node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getY() + node.getScene().getWindow().getY(),
                node.getWidth(),
                node.getHeight());
    }

    void registerDock(Dock dock) {
        docks.add(dock);
    }
}
