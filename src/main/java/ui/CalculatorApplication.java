package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import java.io.File;
import java.io.IOException;

public class CalculatorApplication extends Application {
    private final File tempSave = new File("tempSave.txt");

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CalculatorApplication.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 400);
        stage.setTitle("Calculator");
        stage.setScene(scene);
        stage.show();

        // Front load to avoid freeze on first image creation
        new TeXFormula("1").createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);

        // Handle saving/loading unsaved work
        EquationController controller = fxmlLoader.getController();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                controller.save(tempSave);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public static void main(String[] args) {
        launch();
    }
}