package bblonski.docking;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by bblonski on 8/23/2014.
 */
public class DockPane extends VBox {

    DockPane(Dockable dockable) {
        final HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getStyleClass().add("title");
        final Label title = new Label(dockable.getTitle());
        title.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(title, Priority.ALWAYS);
        Stage stage = new Stage();
        final Button button = new Button("X");
        button.setOnAction(e -> dockable.getDock().setSelected(null));
        button.setStyle("-fx-font-size: 8");
        hBox.getChildren().addAll(title, button);
        final StackPane stackPane = new StackPane();
        getChildren().addAll(hBox, stackPane);
        title.setOnMouseDragged(new DraggableMouseDragHandler(dockable, stage));
        title.setOnMouseReleased(new DraggableMouseReleaseHandler(dockable, stage));
    }
}
