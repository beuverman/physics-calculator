package ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import physics.Equation;
import physics.exceptions.IncompatibleUnitsException;
import physics.exceptions.InvalidDimensionException;

public class EquationController {
    @FXML
    private VBox equationVBox;

    @FXML
    private VBox resultVBox;

    private int size = 1;

    @FXML
    protected void onEquationKeyTyped(KeyEvent ke) {
        Equation eq;
        String result;

        TextField box = (TextField)ke.getSource();
        int index = equationVBox.getChildren().indexOf(box);

        try {
            eq = new Equation(box.getText());
            result = eq.evaluate().toString();
        }
        catch (IncompatibleUnitsException | InvalidDimensionException e) {
            result = e.getMessage();
        }
        catch (Exception e) {
            result = "Error";
        }

        ((TextField)resultVBox.getChildren().get(index)).setText(result);

        if (index == size - 1) {
            if (box.getText().equals(""))
                removeFields();
            else
                addFields();
        }
    }

    private void addFields() {
        TextField equationBox = new TextField();
        TextField resultBox = new TextField();

        resultBox.setEditable(false);
        
        equationBox.setOnKeyTyped(this::onEquationKeyTyped);
        equationVBox.getChildren().add(equationBox);
        resultVBox.getChildren().add(resultBox);

        size++;
    }

    private void removeFields() {
        equationVBox.getChildren().remove(size - 1);
        resultVBox.getChildren().remove(size - 1);

        size--;
    }
}