package bblonski.docking;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

import java.util.List;

/**
 * Created by bblonski on 8/22/2014.
 */
public class Dock extends FlowPane {
    private final ObjectProperty<Dockable> selected = new SimpleObjectProperty<>();
    private final DockContentArea area;
    private final Side side;
    private final DockController dockController;

    Dock(DockController controller, Side side) {
        this.dockController = controller;
        this.side = side;
        area = new DockContentArea(side);
        controller.registerDock(this);
//        setStyle("-fx-border-width: 1; -fx-border-color: red");
//        area.setStyle("-fx-border-color: yellow; -fx-border-width: 1");
        getStyleClass().add("dock");
        if(side == Side.LEFT || side == Side.RIGHT) {
            setOrientation(Orientation.VERTICAL);
        }
        selected.addListener((observable, oldValue, newValue) -> {
            if(oldValue != null) {
                oldValue.getControl().getStyleClass().remove("selected");
            }
            if(newValue == null) {
                getArea().setExpanded(false);
            } else {
                getArea().setExpanded(true);
                area.setContent(newValue.getContent());
                newValue.getControl().getStyleClass().add("selected");
            }
        });
    }

    public DockController getDockController() {
        return dockController;
    }

    public Side getSide() {
        return side;
    }

    public void setSelected(Dockable value) {
        selected.set(value);
    }

    public Dockable getSelected() {
        return selected.get();
    }

    public ObjectProperty<Dockable> selectedProperty() {
        return selected;
    }

    public DockContentArea getArea() {
        return area;
    }
}
