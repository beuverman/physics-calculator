package ui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles interactions with the equation boxes
 */
public class EquationController {
    @FXML
    private VBox equationVBox;

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

    /**
     * Opens the save dialog, allowing for the current equations to be saved to a text file
     * @throws IOException
     */
    @FXML
    public void saveDialog() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Equations");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));
        save(chooser.showSaveDialog(new Popup()));
    }

    public void save(File file) throws IOException {
        if (file == null) return;
        FileWriter fw = new FileWriter(file);
        List<String> equations = getEquations();

        for (String eq : equations) {
            fw.write(eq + "\n");
        }
        fw.close();
    }

    /**
     * Opens the load dialog, allowing multiple text files containing equations to be loaded
     * @throws FileNotFoundException
     */
    @FXML
    public void loadDialog() throws FileNotFoundException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Load Equations");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));
        load(chooser.showOpenMultipleDialog(new Popup()));
    }

    /**
     * Loads each line from the given Files into an Equation Group
     * @param files Files to be loaded
     */
    public void load(List<File> files) throws FileNotFoundException {
        if (files == null) return;
        ArrayList<String> equations = new ArrayList<>();

        for (File file : files) {
            Scanner sc = new Scanner(file);

            while (sc.hasNext())
                equations.add(sc.nextLine());

            sc.close();
        }

        setEquations(equations);
    }

    /**
     * Loads each line from the given File into an Equation Group
     * @param file File to be loaded
     */
    public void load(File file) throws FileNotFoundException {
        if (file == null) return;
        ArrayList<String> equations = new ArrayList<>();
        Scanner sc = new Scanner(file);

        while (sc.hasNext())
            equations.add(sc.nextLine());
        sc.close();

        setEquations(equations);
    }

    /**
     * Clears all Equation Groups and removes all but one
     */
    public void clear() {
        javafx.collections.ObservableList<javafx.scene.Node> children = equationVBox.getChildren();
        children.clear();
        children.add(new EquationGroup());
    }

    /**
     * Sets the text fields of the Equation Groups to the given equations
     * @param equations The equations to be used
     */
    private void setEquations(List<String> equations) {
        javafx.collections.ObservableList<javafx.scene.Node> children = equationVBox.getChildren();
        EquationGroup eg;

        for (int i = 0; i < equations.size(); i++) {
            eg = (EquationGroup) children.get(i);
            eg.setEquation(equations.get(i));
        }
    }

    /**
     * Gets the list of equations currently in the Equation Groups
     * @return Returns the current list of equations as they appear in the input fields
     */
    private List<String> getEquations() {
        javafx.collections.ObservableList<javafx.scene.Node> children = equationVBox.getChildren();
        EquationGroup eg;
        ArrayList<String> equations = new ArrayList<>();

        for (int i = 0; i < children.size(); i++) {
            eg = (EquationGroup) children.get(i);
            equations.add(eg.getEquation());
        }

        return equations;
    }
}