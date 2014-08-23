package bblonski.docking;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
* Created by bblonski on 8/23/2014.
*/
class DraggableMouseDragHandler implements EventHandler<MouseEvent> {
    private Dockable dockable;
    private Stage dragStage;

    public DraggableMouseDragHandler(Dockable dockable, Stage dragStage) {
        this.dockable = dockable;
        this.dragStage = dragStage;
    }

    @Override
    public void handle(MouseEvent t) {
        dragStage.setWidth(dockable.getControl().getWidth() + 10);
        dragStage.setHeight(dockable.getControl().getHeight() + 10);
        dragStage.setX(t.getScreenX());
        dragStage.setY(t.getScreenY());
        dragStage.show();
        Point2D screenPoint = new Point2D(t.getScreenX(), t.getScreenY());
//            bblonski.docking.InsertData data = getInsertData(screenPoint, controller);
//            if (data == null || data.getInsertPane().getTabs().isEmpty()) {
//                controller.getDockStage().hide();
//            } else {
//                int index = data.getIndex();
//                boolean end = false;
//                if (index == data.getInsertPane().getTabs().size()) {
//                    end = true;
//                    index--;
//                }
//                Rectangle2D rect = getAbsoluteRect(data.getInsertPane().getTabs().get(index));
//                if (end) {
//                    controller.getDockStage().setX(rect.getMaxX() + 13);
//                } else {
//                    controller.getDockStage().setX(rect.getMinX());
//                }
//                controller.getDockStage().setY(rect.getMaxY() + 10);
//                controller.getDockStage().show();
//            }
    }
}
