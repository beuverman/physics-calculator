package ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import physics.Equation;
import physics.Parsing;
import physics.exceptions.IncompatibleUnitsException;
import physics.exceptions.InvalidDimensionException;

/**
 * Handles interactions with the equation boxes
 */
public class EquationController {
    @FXML
    private VBox equationVBox;

    @FXML
    private VBox resultVBox;

    private int size = 1;

    @FXML
    /*
     * Event when there is input to an equation box.
     * Attempts to compute the string in the equation box and place the result of the associated result box.
     * If there are too many / too few unused equation boxes, adds or removes as necessary.
     */
    protected void onEquationKeyTyped(KeyEvent ke) {
        Equation eq;
        String result;

        TextField box = (TextField)ke.getSource();
        int index = equationVBox.getChildren().indexOf(box);

        try {
            eq = new Equation(Parsing.tokenizer(box.getText()));
            result = eq.evaluate().toString();
        } catch (Exception e) {
            result = e.getMessage();
        }

        ((TextField)resultVBox.getChildren().get(index)).setText(result);

        if (index == size - 1) {
            if (box.getText().equals(""))
                removeFields();
            else
                addFields();
        }
    }

    /**
     * Adds an additional equation box and result box
     */
    private void addFields() {
        TextField equationBox = new TextField();
        TextField resultBox = new TextField();

        resultBox.setEditable(false);
        
        equationBox.setOnKeyTyped(this::onEquationKeyTyped);
        equationVBox.getChildren().add(equationBox);
        resultVBox.getChildren().add(resultBox);

        size++;
    }

    /**
     * Removes the last equation and result boxes
     */
    private void removeFields() {
        equationVBox.getChildren().remove(size - 1);
        resultVBox.getChildren().remove(size - 1);

        size--;
    }
}