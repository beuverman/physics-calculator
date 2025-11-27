package ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CalculatorController implements Initializable {
    private final File tempSave = new File("tempSave.txt");

    @FXML
    private VBox scene;

    @FXML
    private EquationSet equationSet;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Spinner<Integer> sigFigSpinner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        equationSet = new EquationSet(sigFigSpinner.getValue());
        scrollPane.setContent(equationSet);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        equationSet.getChildren().add(new EquationGroup(equationSet));
        if (tempSave.exists()) {
            try {
                load(tempSave);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        sigFigSpinner.valueProperty().addListener(observable -> equationSet.setSigFigs(sigFigSpinner.getValue()));
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
        List<String> equations = equationSet.getEquationStrings();

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

        equationSet.setEquations(equations);
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

        equationSet.setEquations(equations);
    }

    public void clear() {
        equationSet.clear();
    }
}
