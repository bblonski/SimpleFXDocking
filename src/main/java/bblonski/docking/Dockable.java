package bblonski.docking;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.stage.Stage;

/**
 * Created by bblonski on 8/22/2014.
 */
public class Dockable extends Group {
    private final Button button;
    private ObjectProperty<Dock> dock = new SimpleObjectProperty<>();
    private final DockPane content;
    private String title;

    Dockable(String text, Dock dock) {
        this.title = text;
        button = new Button(title);
        getChildren().add(button);
        this.dock.set(dock);
        dock.getChildren().add(this);
        if (dock.getSide() == Side.LEFT) {
            button.setRotate(-90);
        } else if (dock.getSide() == Side.RIGHT) {
            button.setRotate(90);
        }
        button.getStyleClass().addAll("tab", "dockable");
        button.setOnMouseClicked(e -> {
            if(this.equals(getDock().getSelected())) {
                getDock().setSelected(null);
            } else {
                getDock().setSelected(this);
            }
        });

        Stage stage = new Stage();
        button.setOnMouseDragged(new DraggableMouseDragHandler(this, stage));
        button.setOnMouseReleased(new DraggableMouseReleaseHandler(this, stage));
        this.content = new DockPane(this);
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

    public DockPane getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

}
