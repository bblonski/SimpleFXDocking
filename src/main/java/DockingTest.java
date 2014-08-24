import bblonski.docking.DockController;
import bblonski.docking.DockingFrame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by bblonski on 8/21/2014.
 */
public class DockingTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DockController controller = new DockController();
        DockingFrame parent = controller.getBorderPane();
        final Scene scene = new Scene(parent.getParent(), 1024, 800);
        controller.createDockable("Test 1", parent.getDockBottom());
        controller.createDockable("Test 2", parent.getDockBottom());
        controller.createDockable("Test 3", parent.getDockLeft());
        controller.createDockable("Test 4", parent.getDockRight());
        controller.createDockable("Test 5", parent.getDockTop());
        controller.createDockable("Test 6", parent.getDockCenter());
        controller.createDockable("Test 7", parent.getDockCenter());
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> Platform.exit());
    }

}
