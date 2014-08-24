package bblonski.docking;

import bblonski.docking.Dock;
import bblonski.docking.DockController;
import bblonski.docking.Dockable;
import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

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
            boolean contains = false;
            int oldIndex = oldDock.getChildren().indexOf(dockable);
            for (Dock d : dockable.getDock().getDockController().getAllDocks()) {
                contains = d.localToScreen(d.getLayoutBounds()).contains(screenPoint);
                if (contains) {
                    oldDock.getChildren().remove(dockable);
                    if (dockable.equals(oldDock.getSelected())) {
                        oldDock.setSelected(null);
                    }
                    if (d.getChildren().size() > 0) {
                        dockable.setManaged(false);
                        dockable.setVisible(false);
                        d.getChildren().stream().filter(n -> n instanceof Dockable).forEach(n -> {
                            final Control control = ((Dockable) n).getControl();
                            final TranslateTransition translateTransition =
                                    new TranslateTransition(Duration.seconds(0.2), n);
                            if (d.getOrientation() == Orientation.VERTICAL) {
                                translateTransition.setByY(control.getWidth());
                            } else {
                                translateTransition.setByX(control.getWidth());
                            }
                            translateTransition.setOnFinished(e -> {
                                n.setTranslateX(0);
                                n.setTranslateY(0);
                                dockable.setManaged(true);
                                dockable.setVisible(true);
                            });
                            translateTransition.play();
                        });
                    }
                    d.getChildren().add(0, dockable);
                    dockable.setDock(d);
                    break;
                }
            }
            if(!contains) {
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
//                bblonski.docking.InsertData insertData = getInsertData(screenPoint);
//                if (insertData != null) {
//                    int addIndex = insertData.getIndex();
//                    if (oldTabPane == insertData.getInsertPane() && oldTabPane.getTabs().size() == 1) {
//                        return;
//                    }
//                    oldTabPane.getTabs().remove(bblonski.docking.DraggableTab.this);
//                    if (oldIndex < addIndex && oldTabPane == insertData.getInsertPane()) {
//                        addIndex--;
//                    }
//                    if (addIndex > insertData.getInsertPane().getTabs().size()) {
//                        addIndex = insertData.getInsertPane().getTabs().size();
//                    }
//                    insertData.getInsertPane().getTabs().add(addIndex, bblonski.docking.DraggableTab.this);
//                    insertData.getInsertPane().selectionModelProperty().get().select(addIndex);
//                    return;
//                }
//                if (!detachable) {
//                    return;
//                }
//                final Stage newStage = new Stage();
//                final TabPane pane = new TabPane();
//                tabPanes.add(pane);
//                newStage.setOnHiding(t1 -> tabPanes.remove(pane));
//                getTabPane().getTabs().remove(bblonski.docking.DraggableTab.this);
//                pane.getTabs().add(bblonski.docking.DraggableTab.this);
//                pane.getTabs().addListener((ListChangeListener.Change<? extends Tab> change) -> {
//                    if (pane.getTabs().isEmpty()) {
//                        newStage.hide();
//                    }
//                });
//                newStage.setScene(new Scene(pane));
//                newStage.initStyle(StageStyle.UTILITY);
//                newStage.setX(t.getScreenX());
//                newStage.setY(t.getScreenY());
//                newStage.show();
//                pane.requestLayout();
//                pane.requestFocus();
        }
    }
}
