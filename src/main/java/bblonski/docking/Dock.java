package bblonski.docking;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.FlowPane;

/**
 * Created by bblonski on 8/22/2014.
 */
public class Dock extends FlowPane {
    private DockController controller;
    private final ObjectProperty<Dockable> selected = new SimpleObjectProperty<>();
    private final DockContentArea area = new DockContentArea();

    Dock(DockController controller) {
        this.controller = controller;
        controller.registerDock(this);
        setStyle("-fx-border-width: 1; -fx-border-color: red");
        getStyleClass().add("dock");
        selected.addListener(new ChangeListener<Dockable>() {
            @Override
            public void changed(ObservableValue<? extends Dockable> observable, Dockable oldValue, Dockable newValue) {
                if(oldValue != null) {
                    oldValue.getControl().getStyleClass().remove("selected");
                }
                area.getChildren().clear();
                area.getChildren().add(newValue.getContent());
                newValue.getControl().getStyleClass().add("selected");
            }
        });
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
