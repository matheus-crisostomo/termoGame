module com.termo.termogame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.termo.termogame to javafx.fxml;
    exports com.termo.termogame;
}