package ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.scilab.forge.jlatexmath.TeXIcon;
import physics.Equation;
import physics.Parsing;
import physics.exceptions.IncompatibleUnitsException;
import physics.exceptions.InvalidDimensionException;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXConstants;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;

public class EquationController {
    @FXML
    private VBox equationVBox;

    @FXML
    private VBox resultVBox;

    @FXML
    private VBox imageVBox;

    private int size = 1;

    @FXML
    protected void onEquationKeyTyped(KeyEvent ke) {
        Equation eq;
        String result;

        TextField box = (TextField)ke.getSource();
        int index = equationVBox.getChildren().indexOf(box);

        try {
            eq = new Equation(Parsing.tokenizer(box.getText()));
            result = eq.evaluate().toString();

            ImageView imageView = (ImageView) imageVBox.getChildren().get(index);
            setImage(eq.toLatexString(), imageView);
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

    private void addFields() {
        TextField equationBox = new TextField();
        TextField resultBox = new TextField();
        ImageView imageView = new ImageView();

        resultBox.setEditable(false);
        
        equationBox.setOnKeyTyped(this::onEquationKeyTyped);
        equationVBox.getChildren().add(equationBox);
        resultVBox.getChildren().add(resultBox);
        imageVBox.getChildren().add(imageView);

        size++;
    }

    private void removeFields() {
        equationVBox.getChildren().remove(size - 1);
        resultVBox.getChildren().remove(size - 1);
        imageVBox.getChildren().remove(size - 1);

        size--;
    }

    private void setImage(String latex, ImageView imageView) {
        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(image);
    }
}