package bblonski.docking;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Popup;

/**
 * Created by Brian Blonski on 9/17/2014.
 */
class DragPopup extends Popup {
    private Dockable dockable;

    DragPopup(Dockable dockable) {
        this.dockable = dockable;
        StackPane dragStagePane = new StackPane();
        dragStagePane.getStylesheets().add("docking.css");
        dragStagePane.getStyleClass().addAll("tab", "dockable");
        Text dragText = new Text(dockable.getTitle());
        getContent().add(dragStagePane);
        StackPane.setAlignment(dragText, Pos.CENTER);
        dragStagePane.getChildren().add(dragText);
    }
}
