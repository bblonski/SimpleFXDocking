package bblonski.docking;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * Created by bblonski on 8/22/2014.
 */
public class DockContentArea extends BorderPane {
    private final Side side;
    private boolean expanded = true;
    private final Region dragger;

    DockContentArea(Side side) {
        dragger = new Region();
        dragger.getStyleClass().add("dragger");
        dragger.setMinSize(4, 4);
        this.side = side;
        switch (side) {
            case LEFT:
                StackPane.setAlignment(dragger, Pos.CENTER_RIGHT);
                setRight(dragger);
                dragger.setOnMouseEntered(e -> getScene().setCursor(Cursor.E_RESIZE));
                break;
            case RIGHT:
                setLeft(dragger);
                StackPane.setAlignment(dragger, Pos.CENTER_LEFT);
                dragger.setOnMouseEntered(e -> getScene().setCursor(Cursor.W_RESIZE));
                BorderPane.setAlignment(this, Pos.TOP_RIGHT);
                break;
            case BOTTOM:
                setTop(dragger);
                StackPane.setAlignment(dragger, Pos.TOP_CENTER);
                dragger.setOnMouseEntered(e -> getScene().setCursor(Cursor.N_RESIZE));
                break;
            case TOP:
                setBottom(dragger);
                StackPane.setAlignment(dragger, Pos.BOTTOM_CENTER);
                dragger.setOnMouseEntered(e -> getScene().setCursor(Cursor.S_RESIZE));
                break;
        }
        dragger.setOnMouseExited(e -> getScene().setCursor(Cursor.DEFAULT));
        dragger.setOnMouseDragged(e -> {
            if (side == Side.LEFT) {
                setPrefWidth(e.getSceneX() - localToScene(getLayoutBounds()).getMinX());
            } else if (side == Side.RIGHT) {
                setPrefWidth(localToScene(getLayoutBounds()).getMaxX() - e.getSceneX());
            } else if (side == Side.TOP) {
                setPrefHeight(e.getSceneY() - localToScene(getLayoutBounds()).getMinY());
            } else if (side == Side.BOTTOM) {
                setPrefHeight(localToScene(getLayoutBounds()).getMaxY() - e.getSceneY());
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
        setCenter(node);
    }

}
