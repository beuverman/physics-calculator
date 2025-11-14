package ui;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import physics.Equation;
import physics.Parsing;
import physics.Quantity;

import java.awt.image.BufferedImage;

/**
 * Manages the collection of components associated with a single equation
 */
public class EquationGroup extends HBox {
    private Equation equation;
    private final TextField equationField;
    private final TextField resultField;
    private final ImageView imageField;
    private final EquationSet equationSet;

    /**
     * Creates an empty EquationGroup
     */
    public EquationGroup(EquationSet equationSet) {
        this.equationSet = equationSet;
        equationField = new TextField();
        resultField = new TextField();
        imageField = new ImageView();

        resultField.setEditable(false);
        equationField.setMinWidth(200);
        equationField.setPrefWidth(200);
        resultField.setMinWidth(200);
        resultField.setPrefWidth(200);

        setPrefHeight(200);
        setPrefWidth(600);

        //Handle moving between EquationGroups
        equationField.setOnKeyPressed(keyEvent -> {
            KeyCode keyCode = keyEvent.getCode();
            if (keyCode == KeyCode.UP || (keyCode == KeyCode.ENTER && keyEvent.isShiftDown()))
                goToPrevious();
            else if (keyCode == KeyCode.DOWN || keyCode == KeyCode.ENTER)
                goToNext();
        });

        //Handle parsing equation and updating fields
        equationField.setOnKeyTyped(keyEvent -> equationSet.EquationGroupModified(this));

        this.getChildren().addAll(equationField, resultField, imageField);
    }

    /**
     * Generates an image from a latex string
     * @param latex The string to form an image from
     * @param imageView The component to be painted to
     */
    private void setImage(String latex, ImageView imageView) {
        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(image);
    }

    /**
     * Gets the equation stored in this group.
     * @return Returns the stored Equation.
     */
    public Equation getEquation() {
        return equation;
    }

    /**
     * Gets the equation from this group as it appears in the input field
     * @return Gets the equation from this group
     */
    public String getEquationString() {
        return equationField.getText();
    }

    /**
     * Sets the equation of this group
     * @param equation The equation to be set to
     */
    public void setEquation(String equation) {
        equationField.setText(equation);
    }

    /**
     * Reads what is currently in the input field and attempts to construct an Equation from it.
     * Updates the image field if successful.
     * @return Returns true if an Equation was successfully constructed, false otherwise.
     */
    public boolean parseEquation() {
        try {
            equation = new Equation(Parsing.tokenizer(equationField.getText(), equationSet.getVariableStrings()), equationSet.getVariables());
            setImage(equation.toLatexString(equationSet.getSigFigs()), imageField);
        }
        catch (Exception e) {
            resultField.setText(e.getMessage());
            imageField.imageProperty().set(null);
            return false;
        }

        return true;
    }

    /**
     * Reevalutes the Equation and updates the output field, without reconstructing the Equation from the input field.
     * If evaluation fails, the output field is updated with the corresponding error message.
     * @return Returns the result if the Equation is successfully evaluated. Returns null if not.
     */
    public Quantity evaluate() {
        if (equation != null) {
            try {
                Quantity result = equation.evaluate();
                resultField.setText(equation.evaluate().toString(equationSet.getSigFigs()));
                return result;
            }
            catch (Exception e) {
                resultField.setText(e.getMessage());
            }
        }

        return null;
    }

    /**
     * Checks whether this EquationGroup is empty
     * @return Returns true if the equation is the empty string, false otherwise
     */
    public boolean isEmpty() {
        return equationField.getText().isEmpty();
    }

    public boolean isAssignment() {
        return equation != null && equation.isAssignment();
    }

    /**
     * Since the equationField is the only field that accepts user interation,
     * should the equationGroup receive focus it is given to the equationField.
     */
    @Override
    public void requestFocus() {
        equationField.requestFocus();
    }

    /**
     * Gives focus to the next child in the parent of the equationGroup.
     * If no next child exists, does nothing.
     */
    private void goToNext() {
        ObservableList<Node> children = getParent().getChildrenUnmodifiable();
        int index = children.indexOf(this);

        if (index + 1 != children.size())
            children.get(index + 1).requestFocus();
    }

    /**
     * Gives focus to the previous child in the parent of the equationGroup.
     * If no previous child exists, does nothing.
     */
    private void goToPrevious() {
        ObservableList<Node> children = getParent().getChildrenUnmodifiable();
        int index = children.indexOf(this);

        if (index != 0)
            children.get(index - 1).requestFocus();
    }

    public String toString() {
        return getEquationString();
    }
}