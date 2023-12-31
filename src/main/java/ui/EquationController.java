package ui;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;

/**
 * Handles interactions with the equation boxes
 */
public class EquationController {
    /**
     * Checks whether there are too many or too few equation groups
     * @param parent Component that is holding the EquationGroups. Assumed to be a VBox
     */
    public static void manageEquationCount(Parent parent) {
        javafx.collections.ObservableList<javafx.scene.Node> children = ((VBox) parent).getChildren();
        EquationGroup eqGroup = (EquationGroup) children.get(children.size() - 1);

        if (!eqGroup.isEmpty()) {
            children.add(new EquationGroup());
        }
    }
}