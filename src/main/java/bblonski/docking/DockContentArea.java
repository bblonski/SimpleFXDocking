package bblonski.docking;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.StackPane;

/**
 * Created by bblonski on 8/22/2014.
 */
public class DockContentArea extends StackPane {
    private boolean expanded = true;
    private final Separator dragger;

    DockContentArea(Side side) {
        dragger = new Separator(Orientation.VERTICAL);
        switch (side) {
            case LEFT:
                StackPane.setAlignment(dragger, Pos.CENTER_RIGHT);
                dragger.setOnMouseEntered(e -> getScene().setCursor(Cursor.E_RESIZE));
                break;
            case RIGHT:
                StackPane.setAlignment(dragger, Pos.CENTER_LEFT);
                dragger.setOnMouseEntered(e -> getScene().setCursor(Cursor.W_RESIZE));
                break;
            case BOTTOM:
                StackPane.setAlignment(dragger, Pos.BOTTOM_CENTER);
                dragger.setOrientation(Orientation.HORIZONTAL);
                dragger.setOnMouseEntered(e -> getScene().setCursor(Cursor.S_RESIZE));
                break;
            case TOP:
                StackPane.setAlignment(dragger, Pos.TOP_CENTER);
                dragger.setOrientation(Orientation.HORIZONTAL);
                dragger.setOnMouseEntered(e -> getScene().setCursor(Cursor.N_RESIZE));
                break;
        }
        dragger.setOnMouseExited(e -> getScene().setCursor(Cursor.DEFAULT));
        dragger.setOnMouseDragged(e -> {
            if(side == Side.LEFT) {
                setPrefWidth(e.getSceneX() - localToScene(getLayoutBounds()).getMinX());
            } else if(side == Side.RIGHT) {
                setPrefWidth(localToScene(getLayoutBounds()).getMaxX() - e.getSceneX());
            } else if(side == Side.TOP) {
                setPrefHeight(localToScene(getLayoutBounds()).getMaxY() - e.getSceneY());
            } else if(side == Side.BOTTOM) {
                setPrefHeight(e.getSceneY() - localToScene(getLayoutBounds()).getMinY());
            }
        });
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
        getChildren().addAll(node, dragger);
    }

}
