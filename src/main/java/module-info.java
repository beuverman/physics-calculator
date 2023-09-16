module physicscalculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens ui to javafx.fxml;
    exports ui;
}