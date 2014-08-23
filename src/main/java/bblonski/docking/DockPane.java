package bblonski.docking;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Created by bblonski on 8/23/2014.
 */
public class DockPane extends VBox {

    DockPane(Dockable dockable) {
        getStyleClass().add("titled-pane");
        final HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getStyleClass().add("title");
        final Label title = new Label("Title");
        title.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(title, Priority.ALWAYS);
        final Button button = new Button();
        button.setOnAction(e -> {
            dockable.getDock().setSelected(null);
        });
        button.setMaxHeight(5);
        button.setStyle("-fx-font-size: 8");
        hBox.getChildren().addAll(title, button);
        hBox.getStyleClass().addAll("titled-pane");
        getChildren().addAll(hBox, new StackPane());
    }
}
