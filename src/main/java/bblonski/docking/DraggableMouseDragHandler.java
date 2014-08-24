package bblonski.docking;

import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Optional;

/**
 * Created by bblonski on 8/23/2014.
 */
class DraggableMouseDragHandler implements EventHandler<MouseEvent> {
    private Dockable dockable;
    private Stage dragStage;
    private Optional<Dock> dockOptional = Optional.empty();
    private Optional<Node> previousNode = Optional.empty();

    public DraggableMouseDragHandler(Dockable dockable, Stage dragStage) {
        this.dockable = dockable;
        this.dragStage = dragStage;
        dockOptional = Optional.of(dockable.getDock());
        previousNode = Optional.of(dockable);
    }

    @Override
    public void handle(MouseEvent t) {
        dragStage.setWidth(dockable.getControl().getWidth() + 10);
        dragStage.setHeight(dockable.getControl().getHeight() + 10);
        dragStage.setX(t.getScreenX() - dragStage.getWidth() / 2);
        dragStage.setY(t.getScreenY() - dragStage.getHeight() / 2);
        dragStage.show();
        Optional<Dock> previousDock = dockOptional;
        Point2D screenPoint = new Point2D(t.getScreenX(), t.getScreenY());
        dockOptional = dockable.getDock().getDockController().getAllDocks().stream().filter(d ->
                d.localToScreen(d.getLayoutBounds()).contains(screenPoint.getX(), screenPoint.getY())).findFirst();
        if (previousDock.isPresent() && (!dockOptional.isPresent() || (dockOptional.get() != previousDock.get()))) {
            previousDock.get().getChildren().forEach(e -> {
                final TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.2), e);
                translateTransition.setToX(0);
                translateTransition.setToY(0);
                translateTransition.play();
            });
        }
        if (dockOptional.isPresent()) {
            final Optional<Node> nodeOptional = dockOptional.get().getChildren().stream().filter(b ->
                    b.localToScreen(b.getLayoutBounds()).contains(screenPoint.getX() + b.getTranslateX(), screenPoint.getY() + b.getTranslateY())).findFirst();
            if (nodeOptional.isPresent()) {
                final int nodeIndex;
                if (previousNode.isPresent()) {
                    nodeIndex = dockOptional.get().getChildren().indexOf(nodeOptional.get());
                    dockOptional.get().getChildren().subList(0, nodeIndex).stream().forEach(n -> {
                        final TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.2), n);
                        translateTransition.setToX(0);
                        translateTransition.setToY(0);
                        translateTransition.play();
                    });
                } else {
                    nodeIndex = dockOptional.get().getChildren().size();
                }
                dockOptional.get().getChildren().subList(nodeIndex, dockOptional.get().getChildren().size()).stream()
                        .filter(n -> n instanceof Dockable)
                        .map(n -> (Dockable) n).forEach(n -> {
                    if (n.getTranslateX() == 0 && n.getTranslateY() == 0) {
                        final TranslateTransition translateTransition =
                                new TranslateTransition(Duration.seconds(0.2), n);
                        if (dockOptional.get().getOrientation() == Orientation.VERTICAL) {
                            translateTransition.setByY(n.getControl().getWidth());
                        } else {
                            translateTransition.setByX(n.getControl().getWidth());
                        }
                        dockOptional.get().setOnDragExited(event -> {
                            n.setTranslateX(0);
                            n.setTranslateY(0);
                            dockable.setManaged(true);
                            dockable.setVisible(true);
                            n.setOnMouseExited(null);
                        });
                        dockOptional.get().setOnMouseReleased(event -> {
                            n.setTranslateX(0);
                            n.setTranslateY(0);
                            dockable.setManaged(true);
                            dockable.setVisible(true);
                            n.setOnMouseExited(null);
                        });
                        translateTransition.play();
                    }
                });
            } else {
                dockOptional.get().getChildren().stream().forEach(e -> {
                    final TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.2), e);
                    translateTransition.setToX(0);
                    translateTransition.setToY(0);
                    translateTransition.play();
                });

            }
            previousNode = nodeOptional;
        }
    }
}
