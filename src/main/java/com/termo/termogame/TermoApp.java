package com.termo.termogame;

import com.termo.termogame.controllers.GameController;
import com.termo.termogame.models.GameModel;
import com.termo.termogame.views.TermoView;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class TermoApp extends Application {
    @Override
    public void start(Stage stage) {
        GameModel model = new GameModel();
        TermoView view = new TermoView(model.getTamanhoPalavra(), model.getMaxTentativas());
        GameController controller = new GameController(model, view);
        Image icon = new Image(getClass().getResourceAsStream("/images/icone.png"));


        Scene scene = new Scene((Parent) view.getLayout());
        scene.getStylesheets().add(getClass().getResource("/com/termo/termogame/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("ADVINHAE");
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.show();

        controller.start();

    }


    public static void main(String[] args) {
        launch();
    }
}