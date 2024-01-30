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

import java.awt.image.BufferedImage;

/**
 * Manages the collection of components associated with a single equation
 */
public class EquationGroup extends HBox {
    private TextField equationField;
    private TextField resultField;
    private ImageView imageField;

    /**
     * Creates an empty EquationGroup
     */
    public EquationGroup() {
        equationField = new TextField();
        resultField = new TextField();
        imageField = new ImageView();

        resultField.setEditable(false);

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
        equationField.setOnKeyTyped(keyEvent -> {
            try {
                Equation eq = new Equation(Parsing.tokenizer(equationField.getText()));
                setImage(eq.toLatexString(), imageField);

                resultField.setText(eq.evaluate().toString());
            }
            catch (Exception e) {
                resultField.setText(e.getMessage());
                imageField.imageProperty().set(null);
            }

            EquationController.manageEquationCount(getParent());
        });

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
     * Checks whether this EquationGroup is empty
     * @return Returns true if the equation is the empty string, false otherwise
     */
    public boolean isEmpty() {
        return equationField.getText().isEmpty();
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
}