package bblonski.docking;

import javafx.scene.layout.FlowPane;

/**
 * Created by bblonski on 8/22/2014.
 */
public class Dock extends FlowPane {
    private DockController controller;

    Dock(DockController controller) {
        super();
        this.controller = controller;
        controller.registerDock(this);
        setStyle("-fx-border-width: 1; -fx-border-color: red");
        getStyleClass().add("dock");
        setMinSize(4, 4);
    }
}
