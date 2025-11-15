package com.termo.termogame;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class TermoApp extends Application {
    @Override
    public void start(Stage stage) {
        GameModel model = new GameModel();
        TermoView view = new TermoView(model.getWordLength(), model.getMaxAttempts());
        GameController controller = new GameController(model, view);


        Scene scene = new Scene((Parent) view.getRoot());
        scene.getStylesheets().add(getClass().getResource("/com/termo/termogame/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Termo - JavaFX (MVC)");
        stage.setResizable(false);
        stage.show();

        controller.start();

    }


    public static void main(String[] args) {
        launch();
    }
}