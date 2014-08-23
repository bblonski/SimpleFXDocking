package bblonski.docking;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Created by bblonski on 8/22/2014.
 */
public class DockContentArea extends StackPane {
    private boolean expanded = true;

    DockContentArea() {
    }

    public void setExpanded(boolean isExpanded) {
        this.expanded = isExpanded;
        setManaged(expanded);
        setVisible(expanded);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setContent(Node node) {
        getChildren().clear();
        getChildren().add(node);
    }

}
