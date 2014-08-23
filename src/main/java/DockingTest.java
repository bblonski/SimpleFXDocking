import bblonski.docking.Dock;
import bblonski.docking.DockController;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
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
        Dock dock = controller.createDock();
        BorderPane box1 = new BorderPane();
        HBox left = new HBox();
        VBox bottom = new VBox();
        left.setMinWidth(300);
        box1.setBottom(dock);
        box1.setCenter(dock.getArea());
        controller.createDockable("Test 1", dock);
        controller.createDockable("Test 2", dock);
        controller.createDockable("Test 3", dock);
        Dock dock2 = controller.createDock(Side.LEFT);
        controller.createDockable("Test 4", dock2);
        controller.createDockable("Test 5", dock2);
        Dock dockright = controller.createDock(Side.RIGHT);
        HBox right = new HBox();
        right.getChildren().addAll(dockright.getArea(), dockright);
        left.getChildren().addAll(dock2, dock2.getArea());
        bottom.getChildren().addAll(dock.getArea(), dock);
        parent.setLeft(left);
        parent.setRight(right);
        parent.setBottom(bottom);
        parent.setCenter(new Pane());
        final Scene scene = new Scene(parent, 800, 600);
        primaryStage.setScene(scene);
        scene.getStylesheets().addAll("docking.css");
        primaryStage.show();
    }

}
