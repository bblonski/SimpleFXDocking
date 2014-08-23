import bblonski.docking.Dock;
import bblonski.docking.DockController;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by bblonski on 8/21/2014.
 */
public class DockingTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DockController controller = new DockController();
        BorderPane parent = new BorderPane();
        Dock dockBottom = controller.createDock(Side.BOTTOM);
        BorderPane box1 = new BorderPane();
        HBox right = new HBox();
        HBox left = new HBox();
        VBox bottom = new VBox();
        VBox top = new VBox();
        box1.setBottom(dockBottom);
        box1.setCenter(dockBottom.getArea());
        controller.createDockable("Test 1", dockBottom);
        controller.createDockable("Test 2", dockBottom);
        controller.createDockable("Test 3", dockBottom);
        Dock dockLeft = controller.createDock(Side.LEFT);
        controller.createDockable("Test 4", dockLeft);
        controller.createDockable("Test 5", dockLeft);
        Dock dockRight = controller.createDock(Side.RIGHT);
        Dock dockTop = controller.createDock(Side.TOP);
        controller.createDockable("Test 6", dockRight);
        controller.createDockable("Test 7", dockTop);
        right.getChildren().addAll(dockRight.getArea(), dockRight);
        left.getChildren().addAll(dockLeft, dockLeft.getArea());
        bottom.getChildren().addAll(dockBottom.getArea(), dockBottom);
        top.getChildren().addAll(dockTop, dockTop.getArea());
        parent.setLeft(left);
        parent.setRight(right);
        parent.setBottom(bottom);
        parent.setTop(top);
        final Pane center = new Pane();
        parent.setCenter(center);
        final Scene scene = new Scene(parent, 1024, 800);
        primaryStage.setScene(scene);
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
        scene.getStylesheets().addAll("docking.css");
        primaryStage.show();
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

}
