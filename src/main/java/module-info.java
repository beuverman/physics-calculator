module physicscalculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires ch.obermuhlner.math.big;


    opens ui to javafx.fxml;
    exports ui;
}