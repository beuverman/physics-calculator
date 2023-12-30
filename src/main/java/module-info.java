module physicscalculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires ch.obermuhlner.math.big;
    requires jlatexmath;
    requires java.desktop;
    requires javafx.swing;


    opens ui to javafx.fxml;
    exports ui;
}