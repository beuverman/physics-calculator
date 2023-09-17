package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import physics.Equation;
import physics.exceptions.IncompatibleUnitsException;

public class EquationController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private TextField equationResult;

    @FXML
    protected void onEquationKeyTyped(KeyEvent ke) {
        Equation eq = new Equation(((TextField)ke.getSource()).getText());
        String result;

        try {
            result = eq.evaluate().toString();
        }
        catch (IncompatibleUnitsException e) {
            result = e.getMessage();
        }
        catch (Exception e) {
            result = "Error";
        }

        equationResult.setText(result);
    }
}