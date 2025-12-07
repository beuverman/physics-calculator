module physicscalculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.swing;
    requires ch.obermuhlner.math.big;
    requires jlatexmath;
    requires jscience;
    requires tools.jackson.databind;

    opens ui to javafx.fxml;
    exports ui;
}