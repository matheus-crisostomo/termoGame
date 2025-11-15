module com.termo.termogame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.termo.termogame to javafx.fxml;
    exports com.termo.termogame;
    exports com.termo.termogame.controllers;
    opens com.termo.termogame.controllers to javafx.fxml;
    exports com.termo.termogame.models;
    opens com.termo.termogame.models to javafx.fxml;
    exports com.termo.termogame.views;
    opens com.termo.termogame.views to javafx.fxml;
    exports com.termo.termogame.enums;
    opens com.termo.termogame.enums to javafx.fxml;
}