package bblonski.docking;

import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

/**
 * Created by bblonski on 8/23/2014.
 */
public class DraggableMouseReleaseHandler implements EventHandler<MouseEvent> {
    private Dockable dockable;
    private Popup dragStage;
    private Point2D offset;
    private boolean sameDock;

    public DraggableMouseReleaseHandler(Dockable dockable, Popup dragStage) {
        this.dockable = dockable;
        this.dragStage = dragStage;
    }

    @Override
    public void handle(MouseEvent t) {
        dockable.getDock().getDockController().getDockStage().hide();
        dragStage.hide();
        if (!t.isStillSincePress()) {
            Point2D screenPoint = new Point2D(t.getScreenX(), t.getScreenY());
            Dock oldDock = dockable.getDock();
            dockable.setTranslateX(0);
            dockable.setTranslateY(0);
            final Optional<Dock> dockOptional = dockable.getDock().getDockController().getAllDocks().stream().filter(dock ->
                    dock.localToScreen(dock.getLayoutBounds()).contains(screenPoint)).findFirst();
            if (dockOptional.isPresent()) {
                sameDock = oldDock.equals(dockOptional.get());
                oldDock.getChildren().remove(dockable);
                if (dockable.equals(oldDock.getSelected())) {
                    oldDock.setSelected(null);
                }
                dockOptional.get().getChildren().forEach(n -> {
                    n.setTranslateX(0);
                    n.setTranslateY(0);
                });
                final Optional<Node> nodeOptional = dockOptional.get().getChildren().stream().filter(node ->
                                node.localToScreen(node.getLayoutBounds()).contains(screenPoint.getX() + node.getTranslateX(), screenPoint.getY() + node.getTranslateY())
                ).findFirst();
                if (nodeOptional.isPresent()) {
                    dockOptional.get().getChildren().add(dockOptional.get().getChildren().indexOf(nodeOptional.get()), dockable);
                } else {
                    dockOptional.get().getChildren().add(dockable);
                }
                dockable.setDock(dockOptional.get());
            } else {
                Stage stage = new Stage(StageStyle.UNDECORATED);
                final Dock dock = dockable.getDock().getDockController().createDock(Side.TOP);
                VBox box = new VBox(dock, dock.getArea());
                stage.setScene(new Scene(box));
                stage.setX(screenPoint.getX());
                stage.setY(screenPoint.getY());
                stage.getScene().getStylesheets().addAll("docking.css");
                dock.getChildren().add(dockable);
                dock.setSelected(dockable);
                dock.getChildren().addListener((ListChangeListener<Node>) c -> {
                    if (dock.getChildren().isEmpty() && !sameDock) {
                        dock.getDockController().removeDock(dock);
                        stage.close();
                    }
                });
                dock.setOnMousePressed(e -> offset = new Point2D(e.getScreenX() - stage.getX(), e.getScreenY() - stage.getY()));
                dock.setOnMouseDragged(e -> {
                    if (offset == null) {
                        offset = new Point2D(e.getSceneX(), e.getSceneY());
                    }
                    stage.setX(e.getScreenX() - offset.getX());
                    stage.setY(e.getScreenY() - offset.getY());
                });
                dockable.setDock(dock);
                stage.show();
            }
            System.out.println("Release");
        }
    }
}
