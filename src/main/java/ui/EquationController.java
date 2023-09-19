package ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import physics.Equation;
import physics.exceptions.IncompatibleUnitsException;
import physics.exceptions.InvalidDimensionException;

public class EquationController {
    @FXML
    private TextField equationResult;

    @FXML
    protected void onEquationKeyTyped(KeyEvent ke) {
        Equation eq;
        String result;

        try {
            eq = new Equation(((TextField)ke.getSource()).getText());
            result = eq.evaluate().toString();
        }
        catch (IncompatibleUnitsException | InvalidDimensionException e) {
            result = e.getMessage();
        }
        catch (Exception e) {
            result = "Error";
        }

        equationResult.setText(result);
    }
}