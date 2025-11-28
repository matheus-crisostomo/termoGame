package com.termo.termogame.views;

import com.termo.termogame.enums.EstadoDaLetra;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class TermoView {
    private final BorderPane layout = new BorderPane();
    private final GridPane grid = new GridPane();
    private final Label titulo = new Label("ADVINHAE");
    private final Label mensagem = new Label();
    private final Button restartButton = new Button("Reiniciar");
    private final Button sendButton = new Button("Enviar");

    private final int colunas;
    private final int linhas;
    private final StackPane[][] celulas;
    private final Label[][] letras;

    public TermoView(int cols, int rows) {
        this.colunas = cols;
        this.linhas = rows;
        this.celulas = new StackPane[rows][cols];
        this.letras = new Label[rows][cols];
        criarLayout();
    }

    private void criarLayout() {
        layout.setPrefSize(600, 800);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        titulo.setFont(Font.font("Arial Black", 24));
        titulo.getStyleClass().add("title");
        layout.setTop(titulo);
        BorderPane.setAlignment(titulo, Pos.CENTER);


        for (int r = 0; r < linhas; r++) {
            for (int c = 0; c < colunas; c++) {
                StackPane box = new StackPane();
                box.getStyleClass().add("cell");

                Label lbl = new Label("");
                lbl.getStyleClass().add("cell-letter");
                lbl.setFont(Font.font("Arial Black", 36));

                box.getChildren().add(lbl);

                grid.add(box, c, r);
                celulas[r][c] = box;
                letras[r][c] = lbl;
            }
        }

        VBox bottomBox = new VBox(20);
        HBox insideBox = new HBox(10);
        bottomBox.setAlignment(Pos.CENTER);
        insideBox.setAlignment(Pos.CENTER);

        titulo.setFont(Font.font("Arial Black", 24));
        titulo.getStyleClass().add("titulo");

        mensagem.getStyleClass().add("mensagem");
        mensagem.setFont(Font.font("Arial", 16));

        sendButton.setFont(Font.font("Arial Black", 18));
        sendButton.getStyleClass().add("send-button");

        restartButton.setFont(Font.font("Arial Black", 18));
        restartButton.getStyleClass().add("restart-button");

        insideBox.getChildren().addAll(restartButton, sendButton);
        bottomBox.getChildren().addAll(mensagem,insideBox);

        layout.setCenter(grid);
        layout.setBottom(bottomBox);
        layout.getStyleClass().add("layout");
    }

    public Node getLayout() {
        return layout;
    }

    public Button getRestartButton() {
        return restartButton;
    }

    public void setCellLetter(int row, int col, String ch) {
        letras[row][col].setText(ch == null ? "" : ch);
    }

    public void limparLinhas(int row) {
        for (int c = 0; c < colunas; c++) setCellLetter(row, c, "");
        for (int c = 0; c < colunas; c++) {
            celulas[row][c].getStyleClass().removeAll("gray", "yellow", "green");
            celulas[row][c].getStyleClass().add("cell");
        }
    }

    public void setEstadoDasCelulas(int row, int col, EstadoDaLetra state) {
        StackPane cell = celulas[row][col];
        cell.getStyleClass().removeAll("gray", "yellow", "green");
        switch (state) {
            case GRAY -> cell.getStyleClass().add("gray");
            case YELLOW -> cell.getStyleClass().add("yellow");
            case GREEN -> cell.getStyleClass().add("green");
        }
    }

    public void setMensagem(String txt) {
        mensagem.setText(txt);
    }

    public void celulaFocada(int row, int col) {
        for (int r = 0; r < linhas; r++)
            for (int c = 0; c < colunas; c++)
                celulas[r][c].getStyleClass().remove("focused");

        if (row < linhas && col < colunas && row >= 0 && col >= 0)
            celulas[row][col].getStyleClass().add("focused");
    }

    public int getColunas() { return colunas; }
    public int getLinhas() { return linhas; }
    public Button getSendButton() {
        return sendButton;
    }
}
