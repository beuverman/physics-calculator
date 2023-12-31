package ui;

import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import physics.Equation;
import physics.Parsing;

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

        equationField.setOnKeyTyped(keyEvent -> {
            try {
                Equation eq = new Equation(Parsing.tokenizer(equationField.getText()));
                String result = eq.evaluate().toString();

                resultField.setText(result);
                setImage(eq.toLatexString(), imageField);
            }
            catch (Exception e) {
                resultField.setText(e.getMessage());
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
}