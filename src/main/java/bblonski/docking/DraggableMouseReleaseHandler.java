package bblonski.docking;

import bblonski.docking.Dock;
import bblonski.docking.DockController;
import bblonski.docking.Dockable;
import javafx.animation.TranslateTransition;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
* Created by bblonski on 8/23/2014.
*/
public class DraggableMouseReleaseHandler implements EventHandler<MouseEvent> {
    private Dockable dockable;
    private final DockController controller;
    private Stage dragStage;
    private Text dragText;

    public DraggableMouseReleaseHandler(Dockable dockable, DockController controller, Stage dragStage) {
        this.dockable = dockable;
        this.controller = controller;
        this.dragStage = dragStage;
        dragStage.initStyle(StageStyle.UNDECORATED);
        StackPane dragStagePane = new StackPane();
        dragStagePane.getStyleClass().addAll("tab", "dockable");
        dragText = new Text(dockable.getTitle());
        StackPane.setAlignment(dragText, Pos.CENTER);
        dragStagePane.getChildren().add(dragText);
        dragStage.setScene(new Scene(dragStagePane));
    }

    @Override
    public void handle(MouseEvent t) {
        controller.getDockStage().hide();
        dragStage.hide();
        if (!t.isStillSincePress()) {
            Point2D screenPoint = new Point2D(t.getScreenX(), t.getScreenY());
            Dock oldDock = dockable.getDock();
            int oldIndex = oldDock.getChildren().indexOf(dockable);
            for (Dock d : controller.getAllDocks()) {
                final boolean contains = d.localToScreen(d.getLayoutBounds()).contains(screenPoint);
                if (contains) {
                    oldDock.getChildren().remove(dockable);
                    if (dockable.equals(oldDock.getSelected())) {
                        oldDock.setSelected(null);
                    }
                    if (d.getChildren().size() > 0) {
                        dockable.setManaged(false);
                        dockable.setVisible(false);
                        for (Node n : d.getChildren()) {
                            if (n instanceof Dockable) {
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
                            }
                        }
                    }
                    if (d.getSide() == Side.LEFT) {
                        dockable.getControl().setRotate(-90);
                    } else if (d.getSide() == Side.RIGHT) {
                        dockable.getControl().setRotate(90);
                    } else {
                        dockable.getControl().setRotate(0);
                    }
                    d.getChildren().add(0, dockable);
                    dockable.setDock(d);
                }
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
