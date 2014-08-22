import javafx.animation.TranslateTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Created by bblonski on 8/22/2014.
 */
public class Dockable extends Group {
    private final Button button;
    private Stage dragStage;
    private Text dragText;
    private ObjectProperty<Dock> dock = new SimpleObjectProperty<>();

    Dockable(String text, Dock dock, DockController controller) {
        super();
        button = new Button(text);
        getChildren().add(button);
        this.dock.set(dock);
        dock.getChildren().add(this);
        if(dock.getOrientation() == Orientation.VERTICAL) {
            button.setRotate(-90);
        }
        button.getStyleClass().addAll("tab", "dockable");
        dragStage = new Stage();
        dragStage.initStyle(StageStyle.UNDECORATED);
        StackPane dragStagePane = new StackPane();
        dragStagePane.getStyleClass().addAll("tab", "dockable");
        dragText = new Text(text);
        StackPane.setAlignment(dragText, Pos.CENTER);
        dragStagePane.getChildren().add(dragText);
        dragStage.setScene(new Scene(dragStagePane));
        button.setOnMouseDragged(t -> {
            dragStage.setWidth(button.getWidth() + 10);
            dragStage.setHeight(button.getHeight() + 10);
            dragStage.setX(t.getScreenX());
            dragStage.setY(t.getScreenY());
            dragStage.show();
            Point2D screenPoint = new Point2D(t.getScreenX(), t.getScreenY());
//            InsertData data = getInsertData(screenPoint, controller);
//            if (data == null || data.getInsertPane().getTabs().isEmpty()) {
//                controller.getDockStage().hide();
//            } else {
//                int index = data.getIndex();
//                boolean end = false;
//                if (index == data.getInsertPane().getTabs().size()) {
//                    end = true;
//                    index--;
//                }
//                Rectangle2D rect = getAbsoluteRect(data.getInsertPane().getTabs().get(index));
//                if (end) {
//                    controller.getDockStage().setX(rect.getMaxX() + 13);
//                } else {
//                    controller.getDockStage().setX(rect.getMinX());
//                }
//                controller.getDockStage().setY(rect.getMaxY() + 10);
//                controller.getDockStage().show();
//            }
        });
        button.setOnMouseReleased(t -> {
            controller.getDockStage().hide();
            dragStage.hide();
            if (!t.isStillSincePress()) {
                Point2D screenPoint = new Point2D(t.getScreenX(), t.getScreenY());
                Dock oldDock = getDock();
                int oldIndex = oldDock.getChildren().indexOf(this);
                for (Dock d : controller.getAllDocks()) {
                    final boolean contains = d.localToScreen(d.getLayoutBounds()).contains(screenPoint);
                    if (contains) {
                        oldDock.getChildren().remove(this);
                        if (d.getChildren().size() > 0) {
                            this.setManaged(false);
                            this.setVisible(false);
                            for (Node n : d.getChildren()) {
                                if (n instanceof Dockable) {
                                    final Control control = ((Dockable) n).getControl();
                                    final TranslateTransition translateTransition =
                                            new TranslateTransition(Duration.seconds(0.2), n);
                                    if(d.getOrientation() == Orientation.VERTICAL) {
                                        translateTransition.setByY(control.getWidth());
                                    } else {
                                        translateTransition.setByX(control.getWidth());
                                    }
                                    translateTransition.setOnFinished(e -> {
                                        n.setTranslateX(0);
                                        n.setTranslateY(0);
                                        this.setManaged(true);
                                        this.setVisible(true);
                                    });
                                    translateTransition.play();
                                }
                            }
                        }
                        if(d.getOrientation() == Orientation.VERTICAL) {
                            getControl().setRotate(-90);
                        } else {
                            getControl().setRotate(0);
                        }
                        d.getChildren().add(0, this);
                        this.setDock(d);
                    }
                }
//                InsertData insertData = getInsertData(screenPoint);
//                if (insertData != null) {
//                    int addIndex = insertData.getIndex();
//                    if (oldTabPane == insertData.getInsertPane() && oldTabPane.getTabs().size() == 1) {
//                        return;
//                    }
//                    oldTabPane.getTabs().remove(DraggableTab.this);
//                    if (oldIndex < addIndex && oldTabPane == insertData.getInsertPane()) {
//                        addIndex--;
//                    }
//                    if (addIndex > insertData.getInsertPane().getTabs().size()) {
//                        addIndex = insertData.getInsertPane().getTabs().size();
//                    }
//                    insertData.getInsertPane().getTabs().add(addIndex, DraggableTab.this);
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
//                getTabPane().getTabs().remove(DraggableTab.this);
//                pane.getTabs().add(DraggableTab.this);
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
        });
    }

    Control getControl() {
        return button;
    }

    void setDock(Dock value) {
        dock.set(value);
    }

    Dock getDock() {
         return dock.get();
    }
    private Rectangle2D getAbsoluteRect(Control node) {
        return new Rectangle2D(node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getX() + node.getScene().getWindow().getX(),
                node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getY() + node.getScene().getWindow().getY(),
                node.getWidth(),
                node.getHeight());
    }
}
