import bblonski.docking.Dock;
import bblonski.docking.DockController;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by bblonski on 8/21/2014.
 */
public class DockingTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        final SplitPane value = new SplitPane();
        value.setDividerPositions(.8);
        value.setOrientation(Orientation.VERTICAL);

        final SplitPane middle = new SplitPane();
        middle.setDividerPositions(.2, .8);
        DockController controller = new DockController();

        final TabPane left = controller.createTabPane(Side.LEFT);
        final TabPane center = controller.createTabPane(Side.TOP);
        final TabPane right = controller.createTabPane(Side.RIGHT);
        final TabPane bottom = controller.createTabPane(Side.BOTTOM);
        final TabPane top = controller.createTabPane(Side.TOP);
        top.setMinHeight(80);

        middle.getItems().addAll(left, center, right);
        value.getItems().addAll(middle, bottom);

        BorderPane parent = new BorderPane();
        parent.setTop(top);
        parent.setCenter(value);
        Dock dock = controller.createDock();
        controller.createDockable("Test 1", dock);
        controller.createDockable("Test 2", dock);
        controller.createDockable("Test 3", dock);
        Dock dock2 = controller.createDock();
        dock2.setOrientation(Orientation.VERTICAL);
        controller.createDockable("Test 4", dock2);
        parent.setCenter(value);
        parent.setBottom(dock);
        parent.setLeft(dock2);
        final Scene scene = new Scene(parent, 800, 600);
        primaryStage.setScene(scene);
        scene.getStylesheets().addAll("docking.css");
        primaryStage.show();
    }

}
