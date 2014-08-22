package bblonski.docking;

import javafx.scene.control.TabPane;

/**
* Created by bblonski on 8/22/2014.
*/
class InsertData {

    private final int index;
    private final TabPane insertPane;

    public InsertData(int index, TabPane insertPane) {
        this.index = index;
        this.insertPane = insertPane;
    }

    public int getIndex() {
        return index;
    }

    public TabPane getInsertPane() {
        return insertPane;
    }

}
