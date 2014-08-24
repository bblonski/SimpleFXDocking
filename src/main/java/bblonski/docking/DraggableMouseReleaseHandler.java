package bblonski.docking;

import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Optional;

/**
 * Created by bblonski on 8/23/2014.
 */
public class DraggableMouseReleaseHandler implements EventHandler<MouseEvent> {
    private Dockable dockable;
    private Stage dragStage;

    public DraggableMouseReleaseHandler(Dockable dockable, Stage dragStage) {
        this.dockable = dockable;
        this.dragStage = dragStage;
        dragStage.initStyle(StageStyle.UNDECORATED);
        StackPane dragStagePane = new StackPane();
        dragStagePane.getStyleClass().addAll("tab", "dockable");
        Text dragText = new Text(dockable.getTitle());
        StackPane.setAlignment(dragText, Pos.CENTER);
        dragStagePane.getChildren().add(dragText);
        dragStage.setScene(new Scene(dragStagePane));
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
                if(nodeOptional.isPresent()) {
                    dockOptional.get().getChildren().add(dockOptional.get().getChildren().indexOf(nodeOptional.get()), dockable);
                } else {
                    dockOptional.get().getChildren().add(dockable);
                }
                dockable.setDock(dockOptional.get());
            }
            if (!dockOptional.isPresent()) {
                Stage stage = new Stage(StageStyle.UNDECORATED);
                final Dock dock = dockable.getDock().getDockController().createDock(Side.TOP);
                VBox box = new VBox(dock, dock.getArea());
                stage.setScene(new Scene(box));
                stage.getScene().getStylesheets().addAll("docking.css");
                dock.getChildren().add(0, dockable);
                dock.setSelected(dockable);
                dock.getChildren().addListener((ListChangeListener<Node>) c -> {
                    if (dock.getChildren().isEmpty()) {
                        dock.getDockController().removeDock(dock);
                        stage.close();
                    }
                });
                dock.setOnMouseDragged(e -> {
                    stage.setX(e.getScreenX());
                    stage.setY(e.getScreenY());
                });
                dockable.setDock(dock);
                stage.show();
            }
        }
    }
}
